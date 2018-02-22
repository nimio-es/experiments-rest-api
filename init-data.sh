#/bin/bash
alias nivi="java -jar ./client/target/client-*-jar-with-dependencies.jar"


# The first user have to be an admin
echo Cretion of 'nieves' user \(admin role\)
nivi users create --new-username=nieves --new-password=guapa --as-admin --username=owner --password=password

# After creation of the first admin user, the admin original owner doesn't exist anymore
# It's necessary the use of a different admin user
export NIMIO_USERNAME=nieves
export NIMIO_PASSWORD=guapa
echo Creation of 'saulo' user \(without admin role\)
nivi users create --new-username=saulo --new-password=programador

# Shows all users
echo List all users
nivi users

# Create the customers
echo Register of customers
nivi customers add --first-name Lourdes --last-name Carmona --ndi 123456789X
nivi customers add --first-name Pablo   --last-name Motos   --ndi 987654321Y
nivi customers add --first-name Ruben   --last-name Roque   --ndi 543234199Z
nivi customers add --first-name Marcos  --last-name Santana --ndi 666666666A

# Add images to customers
echo Associate images to customers
nivi customers image set --id 1 --file ./example-data/lourdes.jpg
nivi customers image set --id 2 --file ./example-data/pablo.jpg
nivi customers image set --id 3 --file ./example-data/ruben.jpg
# Marcos has no image

echo List all customers
nivi customers list

echo Show data and image of first customer
nivi customers image get --id 1 --file /tmp/downloaded_image_of_lourdes.jpg --show

# Create some products
nivi products create --reference "XAA0913" --name "Gominolas" --common-price 0.01
nivi products create --reference "PIASS12" --name "Lápices"   --common-price 9.81
nivi products create --reference "IOG123J" --name "Bolsos"    --common-price 54.12
nivi products create --reference "BALLS23" --name "Boliches"  --common-price 0.19
nivi products

# Create some purchases
nivi purchases add --customer-id 1 --product-id 1 --num-of-items 20 --unit-price=0.008
nivi purchases add --customer-id 1 --product-id 3 --num-of-items 1  --unit-price=49.23
nivi purchases add --customer-id 1 --product-id 2 --num-of-items 3  --unit-price=10.21
nivi purchases add --customer-id 2 --product-id 1 --num-of-items 11 --unit-price=0.012
nivi purchases add --customer-id 2 --product-id 4 --num-of-items 2  --unit-price=0.15
nivi purchases add --customer-id 3 --product-id 2 --num-of-items 1  --unit-price=8.99
nivi purchases add --customer-id 3 --product-id 3 --num-of-items 1  --unit-price=60.00
nivi purchases add --customer-id 3 --product-id 4 --num-of-items 5  --unit-price=0.20

echo.
echo.
echo "==============================================================================="
echo "= DATA INITIALIZED                                                            ="
echo "= --------------------------------------------------------------------------- ="
echo "= Set the next environment vars to simplify                                   ="
echo "= the use of nivi command:                                                    ="
echo "=                                                                             ="
echo "= export NIMIO_USERNAME=nieves                                                ="
echo "= export NIMIO_PASSWORD=guapa                                                 ="
echo "=                                                                             ="
echo "= Also you should create an alias to JAR:                                     ="
echo "= alias nivi=\"java -jar ./client/target/client-*-jar-with-dependencies.jar\"   ="
echo "=                                                                             ="
echo "= Type now 'nivi customers' in your command line terminal. For more commands  ="
echo "= use 'nivi help'.                                                            ="
echo "=                                                                             ="
echo "= Welcome to my World!                                                         ="
echo "==============================================================================="
