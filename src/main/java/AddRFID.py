# -*- coding: utf-8 -*-
"""
Created on Mon May  6 13:05:06 2024

@author: Sven
"""

#!/usr/bin/env python

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522

def main():
    print("Select what type of card you want to replace.")
    print("1. Right Card")
    print("2. Left Card")
    print("3. Up Card")
    print("4. Down Card")
    print("5. Reset Card")
    print("6. Easy Card")
    print("7. Medium Card")
    print("8. Hard Card")
    print("9. Turn-Mode Card")
    print("q. Exit")
    main_menu()

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
            add_new_rfid("RFID_EASY")
        elif choice == '7':
            add_new_rfid("RFID_MEDIUM")
        elif choice == '8':
            add_new_rfid("RFID_HARD")
        elif choice == '9':
            add_new_rfid("RFID_TURN")
        elif choice == 'q':
            print("Exiting...")
            break
        else:
            print("Invalid choice..")

def add_new_rfid(selected):
    print("Adding a new " + selected)

    reader = SimpleMFRC522()

    try:
        # Get the card ID from the user
        print("Input Card you wanna add")
        card_id = reader.read()
        # Read the contents of the app.properties file
        with open('/home/pi/target/classes/main/java/app.properties', 'r') as file:
            lines = file.readlines()

        # Find the line corresponding to the selected key
        key_to_update = selected
        found = False
        for i, line in enumerate(lines):
            if line.startswith(key_to_update):
                found = True
                break

        # If the key is found, update its value with the new ID
        if found:
            lines[i] = lines[i].strip() + ', ' + card_id + '\n'
        else:
            print("Key", key_to_update, "not found in app.properties")

        # Write the updated contents back to the app.properties file
        with open('app.properties', 'w') as file:
            file.writelines(lines)

        if found:
            print("RFID added successfully to", key_to_update)

    except Exception as e:
        print("An error occurred:", e)
    finally:
        GPIO.cleanup()

if __name__ == "__main__":
    main()