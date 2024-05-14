import random
import sys
import time

from Adafruit_IO import MQTTClient
from uart import *

AIO_FEED_IDs = ["nutnhan1","nutnhan2"]
AIO_USERNAME = "phductrung"
AIO_KEY = "aio_nejN75jwKy9OGcoMIg6VDJjVlNjN"

def connected(client):
    print("Ket noi thanh cong ...")
    for topic in AIO_FEED_IDs:
        client.subscribe(topic)

def subscribe(client , userdata , mid , granted_qos):
    print("Subscribe thanh cong ...")

def disconnected(client):
    print("Ngat ket noi ...")
    sys.exit (1)

def message(client , feed_id , payload):
    print("Nhan du lieu: " + payload + ", feed ID: " +feed_id)
    if feed_id == "nutnhan1":
        if payload == "0":
            writeData("nutnhan1 tat")
        else:
            writeData("nutnhan1 bat")
    if feed_id == "nutnhan2":
        if payload == "0":
            writeData("nutnhan2 tat")
        else:
            writeData("nutnhan2 bat")

client = MQTTClient(AIO_USERNAME , AIO_KEY)
client.on_connect = connected
client.on_disconnect = disconnected
client.on_message = message
client.on_subscribe = subscribe
client.connect()
client.loop_background()
counter = 10
while True:
    # counter = counter - 1
    # if counter <= 0:
    #     counter = 10
    #     print("Nhan du lieu")
    #     val1 = random.randint(0,100)
    #     client.publish("cambien1", val1)
    #     val2 = random.randint(0,100)
    #     client.publish("cambien2",val2)

    readSerial(client)
    time.sleep(1)
    pass
