package class05;

public class Code02_EditCost {

	/**
	 * 给定两个字符串str1, str2 再给定三个整数ic，dc，rc，分别代表插入，删除，替换 一个字符的代价
	 * 返回str1编辑成str2的最小代价
	 *
	 * str1 = abc, str2
	 *
	 */

	public static int minCost1(String s1, String s2, int ic, int dc, int rc) {
		if (s1 == null || s2 == null) {
			return 0;
		}
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		int N = str1.length + 1;
		int M = str2.length + 1;
		//dp[i][j] 表示：str1前i个字符串 和 str2前J个字符串 最小编辑代价是什么
		int[][] dp = new int[N][M];
		// dp[0][0]  = 0， str1 和 str2 都是空字符串，那么编辑代价为0
		for (int i = 1; i < N; i++) {
			dp[i][0] = dc * i;
		}
		for (int j = 1; j < M; j++) {
			dp[0][j] = ic * j;
		}
		for (int i = 1; i < N; i++) {
			for (int j = 1; j < M; j++) {
				//i-1， j-1 代表的是字符串的位置
				if (str1[i - 1] == str2[j - 1]) {
					//dp[i][j]代表的是 i-1, j-1 位置的代价
					//因为两个字符串 i-1,j-1 字符串相等，所以dp[i][j]的代价就是dp[i-1][j-1]，即前一个位置的代价
					/**
					 * str1: abcde
					 * str1: abcde
					 */
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					//前面都相等，i位置进行替换
					/**
					 * str1: abcdf
					 * str2: abcde
					 */
					dp[i][j] = dp[i - 1][j - 1] + rc;
				}
				//字符串str1的整体是 str的前缀，需要有一个插入代价
				/**
				 * str1: abcd
				 * str2: abcde
				 * 在str1中添加一个元素，只是形式上的添加，真实的str1的长度没有发生变化
				 * str1: ab c
				 * str2: ab c de
				 */
				dp[i][j] = Math.min(dp[i][j], dp[i][j - 1] + ic);
				//把字符串 str1前面i-1个变为str2前j个，str1第i个字符是多余的，需要一个删除代价
				/**
				 * str1: abcdef
				 * str2: abcde
				 */
				dp[i][j] = Math.min(dp[i][j], dp[i - 1][j] + dc);
			}
		}
		return dp[N - 1][M - 1];
	}

	public static int minCost2(String str1, String str2, int ic, int dc, int rc) {
		if (str1 == null || str2 == null) {
			return 0;
		}
		char[] chs1 = str1.toCharArray();
		char[] chs2 = str2.toCharArray();
		char[] longs = chs1.length >= chs2.length ? chs1 : chs2;
		char[] shorts = chs1.length < chs2.length ? chs1 : chs2;
		if (chs1.length < chs2.length) {
			int tmp = ic;
			ic = dc;
			dc = tmp;
		}
		int[] dp = new int[shorts.length + 1];
		for (int i = 1; i <= shorts.length; i++) {
			dp[i] = ic * i;
		}
		for (int i = 1; i <= longs.length; i++) {
			int pre = dp[0];
			dp[0] = dc * i;
			for (int j = 1; j <= shorts.length; j++) {
				int tmp = dp[j];
				if (longs[i - 1] == shorts[j - 1]) {
					dp[j] = pre;
				} else {
					dp[j] = pre + rc;
				}
				dp[j] = Math.min(dp[j], dp[j - 1] + ic);
				dp[j] = Math.min(dp[j], tmp + dc);
				pre = tmp;
			}
		}
		return dp[shorts.length];
	}

	public static void main(String[] args) {
		String str1 = "ab12cd3";
		String str2 = "abcdf";
		System.out.println(minCost1(str1, str2, 5, 3, 2));
		System.out.println(minCost2(str1, str2, 5, 3, 2));

		str1 = "abc";
		str2 = "ab12cd3";
		System.out.println(minCost1(str1, str2, 3, 2, 4));
		System.out.println(minCost2(str1, str2, 3, 2, 4));

		str1 = "";
		str2 = "ab12cd3";
		System.out.println(minCost1(str1, str2, 1, 7, 5));
		System.out.println(minCost2(str1, str2, 1, 7, 5));

		str1 = "abcdf";
		str2 = "";
		System.out.println(minCost1(str1, str2, 2, 9, 8));
		System.out.println(minCost2(str1, str2, 2, 9, 8));

	}

}
