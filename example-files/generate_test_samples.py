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
    (1 * 2 ** 17, '100kb', 'success'),
    (10 * 2 ** 20, '10mb', 'timeout'),
]

for file in source_files:
    filename, ext = os.path.splitext(file)
    for format in formats:
        for size, size_name, result in sizes:
            ffmpeg.input(file).output(f"{filename}_{result}_{size_name}.{format['extension']}", fs=size).overwrite_output().run()
