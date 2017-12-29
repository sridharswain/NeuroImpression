import subprocess
import os

for filename in os.listdir('./train'):
    subprocess.run("ffmpeg -i train/"+filename+" -vcodec png -vframes 1 -an -f rawvideo png/"+filename.split('.')[0]+".png", shell=True, check=True)
