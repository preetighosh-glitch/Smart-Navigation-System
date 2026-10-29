#!/usr/bin/env python3
"""Download a compact OpenStreetMap vector extract for offline Chennai rendering.
Run once while online from the project root: python tools/download_chennai_map.py
No third-party Python packages required.
"""
import json, sys, time, urllib.request, urllib.parse, urllib.error
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'assets' / 'chennai_offline_data.js'
# City-wide bounding box with some surrounding suburbs; OSM data is licensed ODbL.
S, W, N, E = 12.88, 80.12, 13.20, 80.36
QUERY = f'''[out:json][timeout:180];(
way["highway"]({S},{W},{N},{E});
way["natural"="coastline"]({S},{W},{N},{E});
way["waterway"]({S},{W},{N},{E});
way["natural"="water"]({S},{W},{N},{E});
way["landuse"~"park|grass|forest|recreation_ground"]({S},{W},{N},{E});
way["leisure"="park"]({S},{W},{N},{E});
way["railway"~"rail|light_rail|subway|tram"]({S},{W},{N},{E});
);out body geom;'''
ENDPOINTS = [
    'https://overpass-api.de/api/interpreter',
    'https://overpass.kumi.systems/api/interpreter',
    'https://overpass.private.coffee/api/interpreter',
]

def main():
    OUT.parent.mkdir(parents=True, exist_ok=True)
    body = urllib.parse.urlencode({'data': QUERY}).encode()
    data = None
    errors = []
    for endpoint in ENDPOINTS:
        try:
            print(f'Requesting local Chennai OSM extract from {endpoint} ...')
            req = urllib.request.Request(endpoint, data=body, headers={
                'User-Agent': 'ChennaiNeonOfflineMap/1.0 (local student project)',
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            })
            with urllib.request.urlopen(req, timeout=240) as response:
                raw = response.read()
            parsed = json.loads(raw)
            if 'elements' not in parsed:
                raise RuntimeError('Overpass response did not contain map elements')
            data = parsed
            break
        except Exception as exc:
            errors.append(f'{endpoint}: {exc}')
            print(f'  Failed: {exc}')
            time.sleep(2)
    if data is None:
        print('\nCould not download map data. Check internet access and try again later.')
        print('\n'.join(errors))
        sys.exit(1)

    out = {'roads': [], 'waterways': [], 'waterAreas': [], 'parks': [], 'railways': [], 'coastlines': []}
    seen = set()
    for el in data['elements']:
        if el.get('type') != 'way' or not el.get('geometry'):
            continue
        tags = el.get('tags', {})
        coords = [[round(float(p['lat']), 6), round(float(p['lon']), 6)] for p in el['geometry']
                  if 'lat' in p and 'lon' in p]
        if len(coords) < 2:
            continue
        kind = None
        if 'highway' in tags:
            kind = 'roads'
        elif tags.get('natural') == 'coastline':
            kind = 'coastlines'
        elif 'waterway' in tags:
            kind = 'waterways'
        elif tags.get('natural') == 'water' or tags.get('water'):
            kind = 'waterAreas'
        elif tags.get('leisure') == 'park' or tags.get('landuse') in ('park', 'grass', 'forest', 'recreation_ground'):
            kind = 'parks'
        elif tags.get('railway') in ('rail', 'light_rail', 'subway', 'tram'):
            kind = 'railways'
        if not kind:
            continue
        # Clip geometries to a reasonable local extent; preserve all points inside bbox plus ways crossing it.
        if not any(S <= c[0] <= N and W <= c[1] <= E for c in coords):
            continue
        signature = (kind, el.get('id'))
        if signature in seen:
            continue
        seen.add(signature)
        item = {'coords': coords}
        if tags.get('name'):
            item['name'] = tags['name']
        if tags.get('highway'):
            item['highway'] = tags['highway']
        if tags.get('waterway'):
            item['waterway'] = tags['waterway']
        if tags.get('natural'):
            item['natural'] = tags['natural']
        out[kind].append(item)
    count = sum(len(v) for v in out.values())
    if len(out['roads']) < 100:
        print(f'Warning: only {len(out["roads"])} road ways were returned; not writing an incomplete map.')
        sys.exit(2)
    js = '/* OpenStreetMap contributors; ODbL 1.0. Generated locally by tools/download_chennai_map.py. */\n'
    js += 'window.CHENNAI_OFFLINE_DATA = ' + json.dumps(out, ensure_ascii=False, separators=(',', ':')) + ';\n'
    OUT.write_text(js, encoding='utf-8')
    print(f'\nSaved {OUT}')
    print(f'Roads: {len(out["roads"]):,}; waterways: {len(out["waterways"]):,}; water areas: {len(out["waterAreas"]):,}; parks: {len(out["parks"]):,}; railways: {len(out["railways"]):,}; coastlines: {len(out["coastlines"]):,}')
    print(f'Features total: {count:,}; file size: {OUT.stat().st_size / 1024 / 1024:.1f} MB')
    print('Done. The app will render this local data without network access.')

if __name__ == '__main__':
    main()
