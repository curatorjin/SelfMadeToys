from PIL import Image
from PIL import ImageDraw


class Point:
    def __init__(self, r, g, b, x, y):
        self.r = r
        self.g = g
        self.b = b
        self.x = x
        self.y = y

    def get_color(self):
        return Color(self.r, self.g, self.b)


class Color:
    def __init__(self, r, g, b):
        self.r = r
        self.g = g
        self.b = b


def measuring_color(color1, color2):
    return (color1.r - color2.r) ** 2 + (color1.g - color2.g) ** 2 + (color1.b - color2.b) ** 2


def avg_color(points):
    r_total = 0
    g_total = 0
    b_total = 0
    total = len(points)
    for point in points:
        r_total += point.r
        g_total += point.g
        b_total += point.b
    return Color(int(r_total / total), int(g_total / total), int(b_total / total))


def read_pic(pic):
    im = Image.open(pic)
    points = []
    for x in range(im.size[0]):
        for y in range(im.size[1]):
            pixel = im.getpixel((x, y))
            points.append(Point(pixel[0], pixel[1], pixel[2], x, y))
    return im.size, points


def get_nearest(point, anchors):
    point_color = point.get_color()
    distance = measuring_color(point_color, anchors[0])
    nearest = anchors[0]
    for anchor in anchors[1:]:
        n_distance = measuring_color(point_color, anchor)
        if n_distance < distance:
            distance = n_distance
            nearest = anchor
    return nearest


def out_pic(points, size, out):
    im = Image.new('RGB', size, (255, 255, 255))
    imd = ImageDraw.Draw(im)
    for point in points:
        imd.point((point.x, point.y), (point.r, point.g, point.b))
    im.save(out)


def convergence(points, anchors):
    answer = {}
    for point in points:
        anchor = get_nearest(point, anchors)
        if anchor not in answer.keys():
            answer[anchor] = []
        answer[anchor].append(point)
    again = False
    n_anchors = []
    for anchor in answer.keys():
        color_center = avg_color(answer[anchor])
        if not (color_center.r == anchor.r and color_center.r == anchor.r and color_center.r == anchor.r):
            again = True
        n_anchors.append(color_center)
    if again:
        return convergence(points, n_anchors)
    else:
        return answer


def convert_points(points_m):
    out_points = []
    for anchor in points_m.keys():
        for point in points_m[anchor]:
            out_points.append(Point(anchor.r, anchor.g, anchor.b, point.x, point.y))
    return out_points


def main():
    size, points = read_pic('data/test.png')
    anchors = [Color(0, 0, 0), Color(255, 255, 255), Color(128, 128, 128)]
    points_m = convergence(points, anchors)
    out_points = convert_points(points_m)
    out_pic(out_points, size, 'data/out.png')


if __name__ == '__main__':
    main()
