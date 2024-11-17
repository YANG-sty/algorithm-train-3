package class05;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Code01_DeleteMinCost {

	// 题目：
	// 给定两个字符串s1和s2，问s2最少删除多少字符可以成为s1的子串？
	// 比如 s1 = "abcde"，s2 = "axbc"
	// 返回 1

	/**
	 * 子串，连续
	 * 子序列，不连续
	 */

	// 解法一，来自群里的解法：
	// 求出str2所有的子序列，然后按照长度排序，长度大的排在前面。
	// 然后考察哪个子序列字符串和s1的某个子串相等(KMP)，答案就出来了。
	// 分析：
	// 因为题目原本的样本数据中，有特别说明s2的长度很小。所以这么做也没有太大问题，也几乎不会超时。
	// 但是如果某一次考试给定的s2长度远大于s1，这么做就不合适了。
	public static int minCost1(String s1, String s2) {
		List<String> s2Subs = new ArrayList<>();
		process(s2.toCharArray(), 0, "", s2Subs);
		s2Subs.sort(new LenComp());
		for (String str : s2Subs) {
			if (s1.indexOf(str) != -1) { // indexOf底层和KMP算法代价几乎一样，也可以用KMP代替
				return s2.length() - str.length();
			}
		}
		return s2.length();
	}

	public static void process(char[] str2, int index, String path, List<String> list) {
		if (index == str2.length) {
			list.add(path);
			return;
		}
		process(str2, index + 1, path, list);
		process(str2, index + 1, path + str2[index], list);
	}

	public static class LenComp implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			return o2.length() - o1.length();
		}

	}

	// 解法二
	// 我的方法，看的时间比较短，希望同学们积极反馈
	// 生成所有s1的子串
	// 然后考察每个子串和s2的编辑距离(假设编辑距离只有删除动作且删除一个字符的代价为1)
	// 如果s1的长度较小，s2长度较大，这个方法比较合适

	/**
	 * 子串 N^2 个
	 * dp数组 N*M
	 * 时间复杂度 O(N^3 * M)
	 */
	public static int minCost2(String s1, String s2) {
		if (s1.length() == 0 || s2.length() == 0) {
			return s2.length();
		}
		int ans = Integer.MAX_VALUE;
		char[] str2 = s2.toCharArray();
		for (int start = 0; start < s1.length(); start++) {
			for (int end = start + 1; end <= s1.length(); end++) {
				// str1[start....end]
				// substring -> [ 0,1 )
				ans = Math.min(ans, distance(str2, s1.substring(start, end).toCharArray()));
			}
		}
		return ans == Integer.MAX_VALUE ? s2.length() : ans;
	}

	// 求str2到s1sub的编辑距离
	// 假设编辑距离只有删除动作且删除一个字符的代价为1
	public static int distance(char[] str2, char[] s1sub) {
		int row = str2.length;
		int col = s1sub.length;
		int[][] dp = new int[row][col];
		// dp[i][j]的含义：
		// str2[0..i]仅通过删除行为变成s1sub[0..j]的最小代价
		// 可能性一：
		// str2[0..i]变的过程中，不保留最后一个字符(str2[i])，
		// 那么就是通过str2[0..i-1]变成s1sub[0..j]之后，再最后删掉str2[i]即可 -> dp[i][j] = dp[i-1][j] + 1
		// 可能性二：
		// str2[0..i]变的过程中，想保留最后一个字符(str2[i])，然后变成s1sub[0..j]，
		// 这要求str2[i] == s1sub[j]才有这种可能, 然后str2[0..i-1]变成s1sub[0..j-1]即可
		// 也就是str2[i] == s1sub[j] 的条件下，dp[i][j] = dp[i-1][j-1]
		dp[0][0] = str2[0] == s1sub[0] ? 0 : Integer.MAX_VALUE;
		for (int j = 1; j < col; j++) {
			dp[0][j] = Integer.MAX_VALUE;
		}
		for (int i = 1; i < row; i++) {
			dp[i][0] = (dp[i - 1][0] != Integer.MAX_VALUE || str2[i] == s1sub[0]) ? i : Integer.MAX_VALUE;
		}
		for (int i = 1; i < row; i++) {
			for (int j = 1; j < col; j++) {
				dp[i][j] = Integer.MAX_VALUE;
				if (dp[i - 1][j] != Integer.MAX_VALUE) {
					dp[i][j] = dp[i - 1][j] + 1;
				}
				if (str2[i] == s1sub[j] && dp[i - 1][j - 1] != Integer.MAX_VALUE) {
					dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1]);
				}

			}
		}
		return dp[row - 1][col - 1];
	}

	// 解法二的优化

	/**
	 * 子串优化
	 * s1 的子串 0~1
	 * // TODO 待处理，循环逻辑复杂
	 *
	 * 时间复杂度 O(N^2 * M)
	 */
	public static int minCost3(String s1, String s2) {
		if (s1.length() == 0 || s2.length() == 0) {
			return s2.length();
		}
		char[] str2 = s2.toCharArray();
		char[] str1 = s1.toCharArray();
		int M = str2.length;
		int N = str1.length;
		int[][] dp = new int[M][N];
		int ans = M;
		for (int start = 0; start < N; start++) { // 开始的列数
			//第一个字符相等，说明str2, 0到0位置的字符 变为 start位置的字符不用进行删除
			//不相等说明无法通过删除操作变为start位置的字符，所以这里取值为M表示没有结果
			dp[0][start] = str2[0] == str1[start] ? 0 : M;
			//操作start列，从1开始向下的所有行
			for (int row = 1; row < M; row++) {
				//row位置字符 == start位置字符，说明可以删除 row个字符，因为row是从0开始的
				//row位置字符 != start位置字符 && (row-1, start) 位置不为M，
				// 表示前面出现过和 start位置相同的字符，0到row行的字符串变为start位置的字符需要删除的字符数也是row
				dp[row][start] =
						(str2[row] == str1[start] || dp[row - 1][start] != M) ? row : M;
			}
			//dp[M - 1][start]， str1字符串只有start位置的字符时，str2变为str1子串 需要删除字符的个数
			//只有start列也是一个解
			ans = Math.min(ans, dp[M - 1][start]);
			// 以上已经把start列，填好
			// 以下要把dp[...][start+1....N-1]的信息填好
			// start...end end - start +2
			/**
			 * start 相当于开始位置，N是str1的结束位置
			 *
			 * start -> N
			 *
			 * 0～N 列 （0开头的所有子串）
			 * 		行等于 1～M
			 * 1~N 列 （1开头的所有子串）
			 * 		行等于 1～M
			 * 2~N 列  （2开始的所有子串）
			 * 		行等于 1～M
			 * 3~N 列  （3开始的所有子串）
			 * 		行等于 1～M
			 * .....
			 */
			//M是str2的长度，删除字符串的上度不能超过str2的长度
			// end - start 表示存在 end-start + 1 个字符，表示的是str1其中的一个子串
			//当子串的长度 超过 str2 总长度M的时候，str2无法通过删除的方式变为当前的子串
			// end - start + 1 <= M
			// 						==> end -start <= M-1
			// 						==> end - start < M
			//for循环表示的是，start 到 end 整体表示str1的一个子串
			for (int end = start + 1; end < N && end - start < M; end++) {
				// 0... optRow-1 行 不用管，
				// 要想str2 通过删除的方式变为 str1（start， end）这段子串，str2的长度要和str1（start， end）这段子串长度相同
				//因为是从0开始的，所以，optRow = end - start
				int optRow = end - start;
				//计算first位置的值
				//end表示的是s1子串的结尾，当前情况下s1子串的长度为 start 到 end
				// 要想s2通过删除操作 变为 s1（start到end的子串），s2最小的长度要和s1（start到end）子串的长度相同
				// optRow这里表示的是下标， end -start就是行进行对比的最小下标
				// dp[optRow - 1][end - 1] == 0 判断前s1，s2前一个字符是否相等，只有相等的情况，s2才能编辑为s1当前的子串
				dp[optRow][end] = (str2[optRow] == str1[end] && dp[optRow - 1][end - 1] == 0) ? 0 : M;
				//计算first向下的所有的值，继续填充列
				for (int row = optRow + 1; row < M; row++) {
					dp[row][end] = M;
					if (dp[row - 1][end] != M) {
						dp[row][end] = dp[row - 1][end] + 1;
					}
					if (dp[row - 1][end - 1] != M && str2[row] == str1[end]) {
						dp[row][end] = Math.min(dp[row][end], dp[row - 1][end - 1]);
					}
				}
				ans = Math.min(ans, dp[M - 1][end]);
			}
		}
		return ans;
	}

	// 来自学生的做法，时间复杂度O(N * M平方)
	// 复杂度和方法三一样，但是思路截然不同
	public static int minCost4(String s1, String s2) {
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		HashMap<Character, ArrayList<Integer>> map1 = new HashMap<>();
		//获取str1中每个字符出现的位置有哪些
		for (int i = 0; i < str1.length; i++) {
			ArrayList<Integer> list = map1.getOrDefault(str1[i], new ArrayList<Integer>());
			list.add(i);
			map1.put(str1[i], list);
		}
		int ans = 0;
		// 假设删除后的str2必以i位置开头
		// 那么查找i位置在str1上一共有几个，并对str1上的每个位置开始遍历
		// 再次遍历str2一次，看存在对应str1中i后续连续子串可容纳的最长长度
		for (int i = 0; i < str2.length; i++) {
			if (map1.containsKey(str2[i])) {
				//获取str2[i]在str1中的所有位置
				ArrayList<Integer> keyList = map1.get(str2[i]);
				for (int j = 0; j < keyList.size(); j++) {
					//str2[i]一定和 keyList中的每一个位置对应的值相等
					//str1下标
					int cur1 = keyList.get(j) + 1;
					//str2下标
					int cur2 = i + 1;
					int count = 1;
					//遍历str2
					for (int k = cur2; k < str2.length && cur1 < str1.length; k++) {
						//相等的话str1的下标+1，继续下一个字符串比对
						if (str2[k] == str1[cur1]) {
							cur1++;
							count++;
						}
					}
					ans = Math.max(ans, count);
				}
			}
		}
		return s2.length() - ans;
	}

	public static String generateRandomString(int l, int v) {
		int len = (int) (Math.random() * l);
		char[] str = new char[len];
		for (int i = 0; i < len; i++) {
			str[i] = (char) ('a' + (int) (Math.random() * v));
		}
		return String.valueOf(str);
	}

	public static void main(String[] args) {
		int str1Len = 20;
		int str2Len = 10;
		int v = 5;
		int testTime = 10000;
		boolean pass = true;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			String str1 = generateRandomString(str1Len, v);
			String str2 = generateRandomString(str2Len, v);
			int ans1 = minCost1(str1, str2);
			int ans2 = minCost2(str1, str2);
			int ans3 = minCost3(str1, str2);
			int ans4 = minCost4(str1, str2);
			if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
				pass = false;
				System.out.println(str1);
				System.out.println(str2);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println(ans3);
				System.out.println(ans4);
				break;
			}
		}
		System.out.println("test pass : " + pass);
	}

}
