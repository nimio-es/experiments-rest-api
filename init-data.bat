@echo off
SETLOCAL

rem -- The first user have to be an admin
echo Cretion of 'nieves' user (admin role)
call tcli users create --new-username=nieves --new-password=guapa --as-admin --username=owner --password=password

rem -- After creation of the first admin user, the admin original owner doesn't exist anymore
rem -- It's necessary the use of a different admin user
SET THEAM_USERNAME=nieves
SET THEAM_PASSWORD=guapa
echo Creation of 'saulo' user (without admin role)
call tcli users create --new-username=saulo --new-password=programador

rem -- Shows all users
echo List all users
call tcli users

rem -- Create the customers
echo Register of customers
call tcli customers add --first-name Lourdes --last-name Carmona --ndi 123456789X
call tcli customers add --first-name Pablo --last-name Motos --ndi 987654321Y
call tcli customers add --first-name Ruben --last-name Roque --ndi 543234199Z
call tcli customers add --first-name Marcos --last-name Santana --ndi 666666666A

rem -- Add images to customers
echo Associate images to customers
call tcli customers image set --id 1 --file %~dp0/example-data/lourdes.jpg
call tcli customers image set --id 2 --file %~dp0/example-data/pablo.jpg
call tcli customers image set --id 3 --file %~dp0/example-data/ruben.jpg
rem -- Marcos has no image

echo List all customers
call tcli customers list

echo Show data and image of first customer
call tcli customers image get --id 1 --file %TEMP%/downloaded_image_of_lourdes.jpg --show

rem -- Create some products
call tcli products create --reference "XAA0913" --name "Gominolas" --common-price 0.01
call tcli products create --reference "PIASS12" --name "Lápices" --common-price 9.81
call tcli products create --reference "IOG123J" --name "Bolsos" --common-price 54.12
call tcli products create --reference "BALLS23" --name "Boliches" --common-price 0.19
call tcli products

rem -- Create some purchases
call tcli purchases add --customer-id 1 --product-id 1 --num-of-items 20 --unit-price=0.008
call tcli purchases add --customer-id 1 --product-id 3 --num-of-items 1 --unit-price=49.23
call tcli purchases add --customer-id 1 --product-id 2 --num-of-items 3 --unit-price=10.21
call tcli purchases add --customer-id 2 --product-id 1 --num-of-items 11 --unit-price=0.012
call tcli purchases add --customer-id 2 --product-id 4 --num-of-items 2 --unit-price=0.15
call tcli purchases add --customer-id 3 --product-id 2 --num-of-items 1 --unit-price=8.99
call tcli purchases add --customer-id 3 --product-id 3 --num-of-items 1 --unit-price=60.00
call tcli purchases add --customer-id 3 --product-id 4 --num-of-items 5 --unit-price=0.20

echo.
echo.
echo ================================================
echo = DATA INITIALIZED
echo = -------------------------------------------- =
echo = Set the next environment vars to simplify    =
echo = the use of tcli command:                     =
echo =                                              =
echo = SET THEAM_USERNAME=nieves                    =
echo = SET THEAM_PASSWORD=guapa                     =
echo ================================================
echo.

ENDLOCAL
