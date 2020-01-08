from PIL import ImageDraw
from PIL import Image
import os


def get_total_length(target):
    return os.path.getsize(target)


def encode_file(target):
    result = []
    with open(target, 'rb') as f:
        r = f.read()
        for b in r:
            result.append(b)
    return result


def encode_str(sentence):
    b = bytes(sentence, encoding='utf-8')
    result = []
    for ch in b:
        result.append(ch)
    return result


def encrypted_file(target, file_info, output='result.png'):
    if len(target) > 6218880 or len(file_info) > 1920:
        raise Exception
    im = Image.new('RGB', (1920, 1080), (255, 255, 255))
    imd = ImageDraw.Draw(im)
    x, y = 0, 0
    for p in file_info:
        imd.point((x, y), fill=tuple(p))
        x += 1
    x,y = 0,1
    for p in target:
        imd.point((x, y), fill=tuple(p))
        x += 1
        if x >= 1920:
            y += 1
            x = 0
    im.show()
    im.save(output)


def main():
    target = 'test/Theme1.ogg'
    length = get_total_length(target)
    file_encode = encode_file(target)
    file_encode += [255 for i in range(0, (3 - len(file_encode)) % 3)]
    file_encode = [file_encode[i:i + 3] for i in range(0, len(file_encode), 3)]
    file_info = encode_str(str(length))
    file_info += [255 for i in range(0, (3 - len(file_info)) % 3)]
    file_info = [file_info[i:i + 3] for i in range(0, len(file_info), 3)]
    encrypted_file(file_encode, file_info)


if __name__ == '__main__':
    main()
