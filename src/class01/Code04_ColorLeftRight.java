package class01;

public class Code04_ColorLeftRight {

	/**
	 * 枚举分界线，分别对左右两边进行涂色操作，每个位置都这样枚举，求最小值
	 * 辅助数组L记录从左向右的G的个数 （可以省略，分界线遍历的时候能确定左侧出现了多少次G）
	 * 辅助数组R记录从右向左的R的个数 （可以省略，遍历数组获取R的总个数，在后续分界线遍历的时候对减去已经出现的R的个数就是右侧R的个数）
	 * 因为左侧的要把G变为R， 右侧要把R变为G
	 */
	/**
	 * 方式
	 * @param s
	 * @return
	 */
	// RGRGR -> RRRGG
	public static int minPaint(String s) {
		if (s == null || s.length() < 2) {
			return 0;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		int rAll = 0;
		for (int i = 0; i < N; i++) {
			rAll += str[i] == 'R' ? 1 : 0;
		}
		int ans = rAll; // 如果数组所有的范围，都是右侧范围，都变成G
		int left = 0;
		for (int i = 0; i < N - 1; i++) { // 0..i 左侧 n-1..N-1
			left += str[i] == 'G' ? 1 : 0;
			rAll -= str[i] == 'R' ? 1 : 0;
			ans = Math.min(ans, left + rAll);
		}
		// 0...N-1 左全部 右无
		ans = Math.min(ans, left + (str[N - 1] == 'G' ? 1 : 0));
		return ans;
	}

	public static void main(String[] args) {
		String test = "GGGGGR";
		System.out.println(minPaint(test));
	}

}
