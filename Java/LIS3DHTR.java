// Distributed with a free-will license.
// Use it any way you want, profit or free, provided it fits in the licenses of its associated works.
// LIS3DHTR
// This code is designed to work with the LIS3DHTR_I2CS I2C Mini Module available from ControlEverything.com.
// https://www.controleverything.com/content/Accelorometer?sku=LIS3DHTR_I2CS#tabs-0-product_tabset-2

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;

public class LIS3DHTR
{
	public static void main(String args[]) throws Exception
	{
		// Create I2C bus
		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		// Get I2C device, LIS3DHTR I2C address is 0x18(24)
		I2CDevice device = bus.getDevice(0x18);

		// Select control register1
		// X, Y and Z axis enabled, power on mode, data rate o/p 10Hz
		device.write(0x20, (byte)0x27);
		// Select control register4
		// Full scale +/- 2g, continuous update
		device.write(0x23, (byte)0x00);
		Thread.sleep(300);

		// Read 6 bytes of data
		// xAccl lsb, xAccl msb, yAccl lsb, yAccl msb, zAccl lsb, zAccl msb
		byte[] data = new byte[6];
		data[0] = (byte)device.read(0x28);
		data[1] = (byte)device.read(0x29);
		data[2] = (byte)device.read(0x2A);
		data[3] = (byte)device.read(0x2B);
		data[4] = (byte)device.read(0x2C);
		data[5] = (byte)device.read(0x2D);

		// Convert the data
		int xAccl = ((data[1] & 0xFF) * 256 + (data[0] & 0xFF));
		if(xAccl > 32767)
		{
			xAccl -= 65536;
		}

		int yAccl = ((data[3] & 0xFF) * 256 + (data[2] & 0xFF));
		if(yAccl > 32767)
		{
			yAccl -= 65536;
		}

		int zAccl = ((data[5] & 0xFF) * 256 + (data[4] & 0xFF));
		if(zAccl > 32767)
		{
			zAccl -= 65536;
		}

		// Output data to screen
		System.out.printf("Acceleration in X-Axis : %d %n", xAccl);
		System.out.printf("Acceleration in Y-Axis : %d %n", yAccl);
		System.out.printf("Acceleration in Z-Axis : %d %n", zAccl);
	}
}
