import time

import frida
# 获取通过USB连接的设备
device = frida.get_usb_device()
# 1 包名修改
pid = device.spawn(["com.better.learn.fridademo"])
device.resume(pid)
time.sleep(1)  # Without it Java.perform silently fails
session = device.attach(pid)
# 加载的js
with open("s1.js") as f:
    script = session.create_script(f.read())
script.load()

# prevent the python script from terminating
# raw_input()
# https://stackoverflow.com/questions/954834/how-do-i-use-raw-input-in-python-3
input()
