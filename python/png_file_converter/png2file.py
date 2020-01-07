from PIL import Image


def parse_length(arr):
    bs = bytes(arr)
    file_size = int(bs.decode('utf-8'))
    return file_size


def get_from_jpg(jpg, length, start_x, start_y, reading_info=False):
    im = Image.open(jpg)
    result = []
    for i in range(0, int(length/3)):
        if reading_info and im.getpixel((start_x, start_y)) == (255, 255, 255):
            if 255 in result:
                return result[:result.index(255)]
            else:
                return result
        result += im.getpixel((start_x, start_y))
        start_x += 1
        if start_x >= 1920:
            start_y += 1
            start_x = 0
    return result[:length]


def main():
    target = 'test/result.png'
    length = get_from_jpg(target, 1920, 0, 0, reading_info=True)
    file = get_from_jpg(target, parse_length(length), 0, 1)
    content = bytes(file)
    print(len(content))
    with open('result.ogg','wb') as f:
        f.write(bytes(file))

if __name__ == '__main__':
    main()
