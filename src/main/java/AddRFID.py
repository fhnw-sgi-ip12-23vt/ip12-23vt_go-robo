# -*- coding: utf-8 -*-
"""
Created on Mon May  6 13:05:06 2024

@author: Sven
"""

#!/usr/bin/env python

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522
import configparser

print("Welcome to the Main Menu!")
print("1. Right Card")
print("2. Left Card")
print("3. Up Card")
print("4. Down Card")
print("5. Reset Card")
print("6. Next Card")

def main_menu():
    while True:
            choice = input("Enter your choice: ")
            if choice == '1':
                add_new_rfid("RFID_RIGHT")
            elif choice == '2':
                add_new_rfid("RFID_LEFT")
            elif choice == '3':
                add_new_rfid("RFID_UP")
            elif choice == '4':
                add_new_rfid("RFID_DOWN")
            elif choice == '5':
                add_new_rfid("RFID_RESET")
            elif choice == '6':
                add_new_rfid("RFID_NEXT")
            else:
                print("Invalid choice..")
            
def add_new_rfid(selected):
    print("Adding a new " + selected)
    reader = SimpleMFRC522()

    # Load existing properties
    config = configparser.ConfigParser()
    config.read('app.properties')

    try:
        id, text = reader.read()
        print(id)
        print(text)
        
        # Choose the key you want to update (e.g., RFID_RIGHT)
        key_to_update = selected

        # Add the new ID to the existing IDs for the chosen key
        if key_to_update in config:
            current_ids = config[key_to_update]['ids']
            new_ids = current_ids + ', ' + str(id)
            config[key_to_update]['ids'] = new_ids
        else:
            print("Key", key_to_update, "not found in app.properties")

        # Save the updated properties file
        with open('app.properties', 'w') as configfile:
            config.write(configfile)
            
        print("RFID added successfully to", key_to_update)

    finally:
        GPIO.cleanup()
