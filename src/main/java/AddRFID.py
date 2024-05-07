# -*- coding: utf-8 -*-
"""
Created on Mon May  6 13:05:06 2024

@author: Sven
"""

#!/usr/bin/env python

import RPi.GPIO as GPIO
from mfrc522 import SimpleMFRC522

def main():
    main_menu()

def main_menu():
    while True:
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

def add_string_to_line(filename, key, string_to_add):
    # Read the content of the file
    with open(filename, 'r') as file:
        lines = file.readlines()

    # Search for the key in the content
    for i, line in enumerate(lines):
        if key in line:
            # Modify the line by adding the string
            lines[i] = line.rstrip() + ' ' + string_to_add + '\n'

    # Write the modified content back to the file
    with open(filename, 'w') as file:
        file.writelines(lines)


def add_new_rfid(selected):
    print("Adding a new " + selected)

    reader = SimpleMFRC522()

    try:
        # Get the card ID from the user
        print("Input Card you wanna add")
        card_id, abc = reader.read()
        hex_representation = hex(card_id)
        string_representation = str(hex_representation)
        new_string = string_representation[:-2]
        new_string = new_string[2:]
        print(new_string)
        new_string = ', ' + new_string.upper()
        

        # Find the line corresponding to the selected key
        key_to_update = selected
    
        add_string_to_line('/home/pi/target/classes/main/java/app.properties', key_to_update, new_string)

    except Exception as e:
        print("An error occurred:", e)
    finally:
        GPIO.cleanup()

if __name__ == "__main__":
    main()