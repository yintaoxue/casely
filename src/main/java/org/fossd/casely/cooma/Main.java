package org.fossd.casely.cooma;

import com.alibaba.cooma.ExtensionLoader;

public class Main {

	public static void main(String[] args) {
		ExtensionLoader<Car> extensionLoader = ExtensionLoader.getExtensionLoader(Car.class);

		Car racingCar = extensionLoader.getExtension("racing"); // 获取指定名的扩展实例
		racingCar.run();

		Car sportCar = extensionLoader.getExtension("sport"); // 获取指定名的扩展实例
		sportCar.run();
	}

}
