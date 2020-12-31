import frida
import base64


def log_message(message, payload):
    print("message-> %s ,payload-> %s" %(message,payload))
    if(message["type"] == "send"):
        # print("get->"+message["payload"])
        data = message["payload"].split(":")[1].strip()
        print("get->"+data)
        # data.decode("base64") show error: 'str' object has no attribute 'decode'
        data = str(base64.b64decode(data))
        usr, psw = data.split(":")
        data = str(base64.b64encode(("admin:"+psw).encode()))
        print("send->"+data)
        script.post({"my_data": data})


process = frida.get_usb_device().attach("com.better.learn.fridademo")
with open("way_1.js", 'r', encoding='utf-8') as f:
    script = process.create_script(f.read())
script.on('message', log_message)
script.load()
input()