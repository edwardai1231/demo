import RPi.GPIO as GPIO
import time
GPIO.setmode(GPIO.BOARD)

pin_number = 11
GPIO.setup(pin_number, GPIO.OUT)

frequency_hertz = 50
PWM = GPIO.PWM(pin_number, frequency_hertz)

left_position = 0.40
right_position = 2.5
middle_position = (right_position - left_position) / 2 + left_position

positionList = (right_position, middle_position,
                )
#positionList = (left_position, middle_position,
#                right_position, middle_position*2, right_position*2)
ms_per_cycle = 1000 / frequency_hertz

for i in range(1):
    for position in positionList:
        duty_cycle_percentage = position * 100 / ms_per_cycle
        print("Position: " + str(position))
        print("Duty Cycle: " + str(duty_cycle_percentage))
        print("")
        PWM.start(duty_cycle_percentage)
        time.sleep(.5)


GPIO.cleanup()
