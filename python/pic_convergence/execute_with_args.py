# execute.py 的参数化改版：等比降采样 + 剑士气板最近邻吸附
# 色板解析跳过 GIMP 头 / Columns / 空行 / '#' 分隔行 / index0 透明占位
from PIL import Image
from PIL import ImageDraw

import argparse


class Point:
    def __init__(self, r, g, b, x, y):
        self.r = r
        self.g = g
        self.b = b
        self.x = x
        self.y = y


class Color:
    def __init__(self, r, g, b):
        self.r = r
        self.g = g
        self.b = b


def measuring_color(color1, color2):
    return (color1.r - color2.r) ** 2 + (color1.g - color2.g) ** 2 + (color1.b - color2.b) ** 2


def read_pic(pic, target_size):
    im = Image.open(pic).convert('RGB')
    if target_size is not None:
        # 等比缩放，不拉伸比例
        w, h = im.size
        tw, th = target_size
        scale = min(tw / w, th / h, 1.0)
        if scale < 1.0:
            im = im.resize((max(1, round(w * scale)), max(1, round(h * scale))), Image.LANCZOS)
    points = []
    for x in range(im.size[0]):
        for y in range(im.size[1]):
            pixel = im.getpixel((x, y))
            points.append(Point(pixel[0], pixel[1], pixel[2], x, y))
    return im.size, points


def out_pic(points, size, out):
    im = Image.new('RGB', size, (255, 255, 255))
    imd = ImageDraw.Draw(im)
    for point in points:
        imd.point((point.x, point.y), (point.r, point.g, point.b))
    im.save(out)


def parse_gpl(path):
    """解析 Aseprite/GIMP .gpl 色板，返回 [(r, g, b, name), ...]"""
    colors = []
    with open(path, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.strip()
            if not line or line.startswith('#') or line == 'GIMP Palette' or line.startswith('Name:') or line.startswith('Columns:'):
                continue
            parts = line.split(None, 3)
            if len(parts) < 3:
                continue
            r, g, b = int(parts[0]), int(parts[1]), int(parts[2])
            name = parts[3] if len(parts) > 3 else ''
            # index0 透明占位，不参与吸附
            if 'transparent' in name:
                continue
            colors.append((r, g, b, name))
    return colors


def snap_anchors_to_palette(centers, palette):
    """把每个颜色替换为色板中最近的色"""
    pal_colors = [Color(r, g, b) for r, g, b, _ in palette]
    snapped = []
    for center in centers:
        best = pal_colors[0]
        best_d = measuring_color(center, best)
        for pc in pal_colors[1:]:
            d = measuring_color(center, pc)
            if d < best_d:
                best_d = d
                best = pc
        snapped.append(best)
    return snapped


def parse_size(text):
    if text.lower() == 'none':
        return None
    w, h = text.split('x')
    return (int(w), int(h))


def main():
    parser = argparse.ArgumentParser(description='逐像素最近邻色彩量化 + 色板吸附')
    parser.add_argument('--input', default='data/test.png', help='输入图片路径')
    parser.add_argument('--output', default='data/out.png', help='输出图片路径')
    parser.add_argument('--target-size', default='640x360', type=parse_size, help='降采样目标分辨率上限，如 640x360；传 none 跳过降采样')
    parser.add_argument('--palette', required=True, help='.gpl 色板路径')
    args = parser.parse_args()
    target_size = args.target_size
    size, points = read_pic(args.input, target_size)
    pal = snap_anchors_to_palette(points, parse_gpl(args.palette))
    out_points = [Point(c.r, c.g, c.b, p.x, p.y) for p, c in zip(points, pal)]

    out_pic(out_points, size, args.output)


if __name__ == '__main__':
    main()