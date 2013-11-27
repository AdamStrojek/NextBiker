package info.strojek;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import android.graphics.Color;

public class zwGradientCalculator {
	private TreeMap<Double, Integer> colorList = new TreeMap<Double, Integer>();

	public zwGradientCalculator() {
	}

	public void addColor(double ratio, int color) {
		colorList.put(ratio, color);
	}

	/**
	 * @param ratio
	 *            - value between 0 and 1
	 */
	public int getColor(double ratio) {
		if (ratio <= colorList.firstKey()) {
			return colorList.get(colorList.firstKey());
		}

		if (ratio >= colorList.lastKey()) {
			return colorList.get(colorList.lastKey());
		}

		Map.Entry<Double, Integer> start = null;
		Map.Entry<Double, Integer> end = null;

		Iterator<Map.Entry<Double, Integer>> it = colorList.entrySet()
				.iterator();

		while (it.hasNext()) {
			Map.Entry<Double, Integer> color = it.next();

			if (color.getKey() >= ratio) {
				end = color;
				break;
			}
			start = color;
		}

		double start_ratio = start.getKey();
		double end_ratio = end.getKey();
		
		double length = end_ratio - start_ratio;
		
		int start_color = start.getValue();
		int end_color = end.getValue();
		
		ratio = ((ratio-start_ratio)/length);

		int red = (int) (Color.red(start_color) * (1 - ratio) + Color.red(end_color) * ratio);
		int green = (int) (Color.green(start_color) * (1 - ratio) + Color.green(end_color) * ratio);
		int blue = (int) (Color.blue(start_color) * (1 - ratio) + Color.blue(end_color) * ratio);
		int alpha = (int) (Color.alpha(start_color) * (1 - ratio) + Color.alpha(end_color) * ratio);
		return Color.argb(alpha, red, green, blue);
	}
}
