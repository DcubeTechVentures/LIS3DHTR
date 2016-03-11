// Distributed with a free-will license.
// Use it any way you want, profit or free, provided it fits in the licenses of its associated works.
// LIS3DHTR
// This code is designed to work with the LIS3DHTR_I2CS I2C Mini Module available from ControlEverything.com.
// https://www.controleverything.com/content/Accelorometer?sku=LIS3DHTR_I2CS#tabs-0-product_tabset-2

#include<Wire.h>

// LIS3DHTR I2C address is 18(24)
#define Addr 0x18

void setup() 
{
  // Initialise I2C communication as MASTER
  Wire.begin();
  // Initialise Serial Communication, set baud rate = 9600
  Serial.begin(9600);
  
  // Start I2C Transmission
  Wire.beginTransmission(Addr);
  // Select control register 1
  Wire.write(0x20);
  // Enable X, Y, Z axis, power on mode, data rate selection = 10Hz
  Wire.write(0x27);
  // Stop I2C Transmission
  Wire.endTransmission();
  
  // Start I2C Transmission
  Wire.beginTransmission(Addr);
  // Select control register 4
  Wire.write(0x23);
  // Set continuous update, +/- 2g, Self-test enabled
  Wire.write(0x00);
  // Stop I2C Transmission
  Wire.endTransmission();
  delay(300);
}

void loop()
{
  unsigned int data[6];
  for(int i = 0; i < 6; i++)
  {
    // Start I2C Transmission
    Wire.beginTransmission(Addr);
    // Select data register
    Wire.write((40 + i));
    // Stop I2C Transmission
    Wire.endTransmission();
    
    // Request 1 byte of data
    Wire.requestFrom(Addr,1);
    
    // Read 6 bytes of data
    // xAccl lsb, xAccl msb, yAccl lsb, yAccl msb, zAccl lsb, zAccl msb
    if(Wire.available()==1)
    {
      data[i] = Wire.read();
    }
    delay(300);
  }
  
  // Convert the data
  int xAccl = ((data[1] * 256) + data[0]);
  int yAccl = ((data[3] * 256) + data[2]);
  int zAccl = ((data[5] * 256) + data[4]);
  
  // Output data to serial monitor
  Serial.print("Acceleration in X-Axis :");
  Serial.println(xAccl);
  Serial.print("Acceleration in Y-Axis :");
  Serial.println(yAccl);
  Serial.print("Acceleration in Z-Axis :");
  Serial.println(zAccl);  
  delay(500);
}

