package class04;

import java.util.Arrays;
import java.util.Comparator;

public class Code05_EnvelopesProblem {

	/**
	 * 每个信封都右长和宽两个纬度的数据，A信封如果想套在B信封里面，A信封必须在长和宽上都小于B信封
	 * 现在右一批信封，返回最大的嵌套层数
	 *
	 * 方法：
	 * 1。将一维数据由小到大排序，二维数据由大到小排序
	 * 2。将二维数据拿出来
	 * 3。二维数据的最长递增子序列就是结果
	 */
	public static class Envelope {
		public int l;
		public int h;

		public Envelope(int weight, int hight) {
			l = weight;
			h = hight;
		}
	}

	public static class EnvelopeComparator implements Comparator<Envelope> {
		/**
		 * 按照长度升序，高度降序
		 */
		@Override
		public int compare(Envelope o1, Envelope o2) {
			return o1.l != o2.l ? o1.l - o2.l : o2.h - o1.h;
		}
	}

	public static Envelope[] getSortedEnvelopes(int[][] matrix) {
		Envelope[] res = new Envelope[matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			res[i] = new Envelope(matrix[i][0], matrix[i][1]);
		}
		//按照长度升序，高度降序
		Arrays.sort(res, new EnvelopeComparator());
		return res;
	}

	public static int maxEnvelopes(int[][] matrix) {
		//按照长度升序，高度降序
		Envelope[] envelopes = getSortedEnvelopes(matrix);
		int[] ends = new int[matrix.length];
		ends[0] = envelopes[0].h;
		int right = 0;
		int l = 0;
		int r = 0;
		int m = 0;
		for (int i = 1; i < envelopes.length; i++) {
			l = 0;
			r = right;
			while (l <= r) {
				m = (l + r) / 2;
				if (envelopes[i].h > ends[m]) {
					l = m + 1;
				} else {
					r = m - 1;
				}
			}
			right = Math.max(right, l);
			ends[l] = envelopes[i].h;
		}
		return right + 1;
	}

	public static void main(String[] args) {
		int[][] test = {
				{ 1, 3 },
				{ 1, 2 },
				{ 2, 2 },
				{ 2, 3 },
				{ 2, 4 },
				{ 3, 6 },
				{ 3, 4 },
				{ 3, 2 },
				{ 4, 5 }
				 };
		System.out.println(maxEnvelopes(test));
	}
}
