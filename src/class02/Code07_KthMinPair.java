package class02;

import java.util.Arrays;
import java.util.Comparator;

public class Code07_KthMinPair {
	/**
	 * 给定一个长度为N的整数数组arr，一定可以组成N^2个数值对；
	 * 例如[3,1,2]，你需要将它们之间两两组合出来的数中第k小的组合找出来；
	 * 排序规则为：先按照（x,y）x的大小进行排序，如果x相同，再按照y进行排序
	 * 上面示例中数组的组合为:
	 * (3,3),(3,1),(3,2),(1,3),(1,1),(1,2),(2,3,),(2,1),(2,2)
	 * 排序之后:
	 * (1,1),(1,2),(1,3),(2,1),(2,2),(2,3),(3,1),(3,2),(3,3)
	 * 如果要找出第3小的组合，那就是 (1,3)
	 *
	 * 数组可以存在重复的数值 {1,1,2,2,3,3,3,4,5,5}
	 *
	 *
	 * 方法2：
	 * 1。整体排序
	 * 2。k-1/N 求出第一个数的索引
	 * 3。<f的数据的个数 a， =f的数据的个数 b
	 * 4。k-a*N -> s, s-1/b ->第二个数的索引
	 *
	 * 方法3：
	 *
	 */

	public static class Pair {
		public int x;
		public int y;

		Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	public static class PairComparator implements Comparator<Pair> {

		@Override
		public int compare(Pair arg0, Pair arg1) {
			return arg0.x != arg1.x ? arg0.x - arg1.x : arg0.y - arg1.y;
		}

	}

	// O(N^2 * log (N^2))的复杂度，你肯定过不了
	// 返回的int[] 长度是2，{3,1} int[2] = [3,1]
	public static int[] kthMinPair1(int[] arr, int k) {
		int N = arr.length;
		if (k > N * N) {
			return null;
		}
		Pair[] pairs = new Pair[N * N];
		int index = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				pairs[index++] = new Pair(arr[i], arr[j]);
			}
		}
		Arrays.sort(pairs, new PairComparator());
		return new int[] { pairs[k - 1].x, pairs[k - 1].y };
	}

	// O(N*logN)的复杂度，你肯定过了
	public static int[] kthMinPair2(int[] arr, int k) {
		int N = arr.length;
		if (k > N * N) {
			return null;
		}
		// O(N*logN)
		Arrays.sort(arr);
		// 第K小的数值对，第一维数字，是什么 是arr中
		int fristNum = arr[(k - 1) / N];
		int lessFristNumSize = 0;// 数出比fristNum小的数有几个
		int fristNumSize = 0; // 数出==fristNum的数有几个
		// <= fristNum
		for (int i = 0; i < N && arr[i] <= fristNum; i++) {
			if (arr[i] < fristNum) {
				lessFristNumSize++;
			}
//			else {
//				fristNumSize++;
//			}
			if (arr[i] == fristNum) {
				fristNumSize++;
			}
		}
		int rest = k - (lessFristNumSize * N);
		return new int[] { fristNum, arr[(rest - 1) / fristNumSize] };
	}

	// O(N)的复杂度，你肯定蒙了
	public static int[] kthMinPair3(int[] arr, int k) {
		int N = arr.length;
		if (k > N * N) {
			return null;
		}
		// 在无序数组中，找到第K小的数，返回值
		// 第K小，以1作为开始
		int fristNum = getMinKth(arr, (k - 1) / N);
		// < fristNum 的个数，
		int lessFristNumSize = 0;
		// == fristNum 的个数
		int fristNumSize = 0;
		for (int i = 0; i < N; i++) {
			if (arr[i] < fristNum) {
				lessFristNumSize++;
			}
			if (arr[i] == fristNum) {
				fristNumSize++;
			}
		}
		int rest = k - (lessFristNumSize * N);
		return new int[] { fristNum, getMinKth(arr, (rest - 1) / fristNumSize) };
	}

	// 改写快排，时间复杂度O(N)
	// 在无序数组arr中，找到，如果排序的话，arr[index]的数是什么？
	//获取第k小的值， k这里表示的索引下标
	public static int getMinKth(int[] arr, int index) {
		int L = 0;
		int R = arr.length - 1;
		int pivot = 0;
		int[] range = null;
		while (L < R) {
			//在L到R之间随机获取一个pivot
			pivot = arr[L + (int) (Math.random() * (R - L + 1))];
			//arr按照pivot 进行分组，小的放前面，大的放后面
			//返回
			range = partition(arr, L, R, pivot);
			if (index < range[0]) {
				R = range[0] - 1;
			} else if (index > range[1]) {
				L = range[1] + 1;
			} else {
				return pivot;
			}
		}
		return arr[L];
	}

	/**
	 * 荷兰国旗问题，
	 * 将arr[L...R] 范围内的数，按照小于、等于、大于 pivot 分别放在一起，
	 * 返回等于区域的起止位置
	 */
	public static int[] partition(int[] arr, int L, int R, int pivot) {
		int less = L - 1;
		int more = R + 1;
		int cur = L;
		while (cur < more) {
			if (arr[cur] < pivot) {
				swap(arr, ++less, cur++);
			} else if (arr[cur] > pivot) {
				swap(arr, cur, --more);
			} else {
				cur++;
			}
		}
		return new int[] { less + 1, more - 1 };
	}

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	// 为了测试，生成值也随机，长度也随机的随机数组
	public static int[] getRandomArray(int max, int len) {
		int[] arr = new int[(int) (Math.random() * len) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
		}
		return arr;
	}

	// 为了测试
	public static int[] copyArray(int[] arr) {
		if (arr == null) {
			return null;
		}
		int[] res = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			res[i] = arr[i];
		}
		return res;
	}

	// 随机测试了百万组，保证三种方法都是对的
	public static void main(String[] args) {

//		int[] arr = {0, 1, 1, 2, 3, 4};
//		int L = 0;
//		int R = arr.length - 1;
//		int pivot = 2;
//		int[] partition = partition(arr, L, R, pivot);
//		System.out.println(partition[0]);
//		System.out.println(partition[1]);

		int max = 100;
		int len = 30;
		int testTimes = 100000;
		System.out.println("test bagin, test times : " + testTimes);
		for (int i = 0; i < testTimes; i++) {
			int[] arr = getRandomArray(max, len);
			int[] arr1 = copyArray(arr);
			int[] arr2 = copyArray(arr);
			int[] arr3 = copyArray(arr);
			int N = arr.length * arr.length;
			int k = (int) (Math.random() * N) + 1;
			int[] ans1 = kthMinPair1(arr1, k);
			int[] ans2 = kthMinPair2(arr2, k);
			int[] ans3 = kthMinPair3(arr3, k);
			if (ans1[0] != ans2[0] || ans2[0] != ans3[0] || ans1[1] != ans2[1] || ans2[1] != ans3[1]) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
