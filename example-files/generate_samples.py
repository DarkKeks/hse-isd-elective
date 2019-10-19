import os
import sys
import ffmpeg

source_files = sys.argv[1:]

formats = [
    {'extension': 'mp4'},
    {'extension': 'mkv'},
    {'extension': 'mov'},
    {'extension': 'flv'},
]

sizes = [
    (1 * 2 ** 17, '100kb'),
    (1 * 2 ** 19, '500kb'),
    (1 * 2 ** 20, '1mb'),
    (2 * 2 ** 20, '2mb'),
    (5 * 2 ** 20, '5mb'),
    (10 * 2 ** 20, '10mb'),
]

for file in source_files:
    filename, ext = os.path.splitext(file)
    for format in formats:
        for size, size_name in sizes:
            ffmpeg.input(file).output(f"{filename}_{size_name}.{format['extension']}", fs=size).run()
