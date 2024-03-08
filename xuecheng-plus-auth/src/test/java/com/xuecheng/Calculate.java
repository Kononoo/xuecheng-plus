package com.xuecheng;

import org.springframework.security.core.parameters.P;

/**
 * @ClassName: Calculate
 * @Package: com.xuecheng
 * @Description: 算法练习题
 * @Author: Ronan
 * @Create 2024/3/8 - 14:28
 * @Version: v1.0
 */
public class Calculate {

    /**
     * 快速排序
     * @param nums
     * @param start
     * @param end
     */
    public static void quickSort(int[] nums, int start, int end) {
        if (start < end) {
            int base = nums[start];
            int left = start;
            int right = end;
            while (left < right) {
                while (left < right && nums[right] >= base) {
                    right--;
                }
                nums[left] = nums[right];
                while (left < right && nums[left] <= base) {
                    left++;
                }
                nums[right] = nums[left];
            }
            nums[left] = base;
            // 左边
            quickSort(nums, start, left - 1);
            // 右边
            quickSort(nums, left + 1, end);
        }
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 5, 2, 3, 7};
        System.out.println("排序前：");
        for (int num : nums) {
            System.out.print(num);
        }
        quickSort(nums, 0, nums.length - 1);
        System.out.println("排序后：");
        for (int num : nums) {
            System.out.print(num);
        }
    }

    /**
     * 滑动窗口
     * @param s
     * @param nums
     * @return
     */
    public static int minSubArrayLen(int s, int[] nums) {
        int left = 0;
        int subLength;
        int sum = 0;
        int result = Integer.MAX_VALUE;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            if (sum >= s) {
                subLength = right - left + 1;
                result = Math.min(subLength, result);
                sum -= nums[left++];
            }
        }
        return result == Integer.MAX_VALUE ? 0 : result;
    }

    /**
     * 斐波那契数列 - 非递归
     * @param num 第n项
     * @return
     */
    public static int Fibonacci1(int num) {
        if (num <= 0) {
            return 0;
        }
        if (num == 1 || num == 2) {
            return 1;
        }
        int first = 1;
        int second = 1;
        int third = 0;
        for (int i = 3; i <= num; i++) {
            third = first + second;
            first = second;
            second = third;
        }
        return third;
    }

    public static int Fibonacci2(int num) {
        if (num <= 0) {
            return 0;
        }
        if (num == 1 || num == 2) {
            return 1;
        }
        return Fibonacci2(num - 1) + Fibonacci2(num - 2);
    }

    /**
     * @param number 台阶
     * @return 多少种跳法
     * @description 找规律分析
     * @description 跳台阶问题: 一只青蛙一次可以跳上 1 级台阶，也可以跳上 2 级。求该青蛙跳上一个 n 级的台阶总共有多少种跳法。
     */
    public static int JumpFloor(int number) {
        if (number <= 0) {
            return 0;
        }
        if (number == 1) {
            return 1;
        }
        if (number == 2) {
            return 2;
        }
        int first = 1, second = 2, third = 0;
        for (int i = 0; i < number; i++) {
            third = first + second;
            first = second;
            second = third;
        }
        return third;
    }

    /**
     * 变态跳台阶问题
     * @param number
     * @return
     */
    public static int jumpFloorII(int number) {
        /**
         * 分析：f(n) = f(n-1) + f(n-2) + ... + f(1)
         *    则 f(n) = 2f(n-1) 又f(1) = 1
         *    得 f(n) = 2 ^ (number-1)
         */
        return 1 << (number - 1); // 1 << --number
    }

    /**
     * @description 二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
     * @param target
     * @param array
     * @return
     */
    public boolean Find(int target, int[][] array) {
        int row = array.length - 1;
        int column = 0;
        while (row >= 0 && column < array[0].length) {
            if (array[row][column] > target) {
                row--;
            } else if (array[row][column] < target) {
                column++;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 替换空格
     * @param str
     */
    public static String replaceBlank(String str) {
//        str.replaceAll(" ", "%20");
//        str.replaceAll("\\s", "%20");
        StringBuilder string = new StringBuilder(str);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            String item = String.valueOf(string.charAt(i)));
            if (" ".equals(item)) {
                result.append("%20");
            } else {
                result.append(item);
            }
        }
        return result.toString();
    }

    /**
     * @description 定一个 double 类型的浮点数 base 和 int 类型的整数 exponent。求 base 的 exponent 次方
     * @param base
     * @param exponent
     * @return
     */
    public double powerAnother(double base, int exponent) {
        double result = 1.0;
        for (int i = 0; i < Math.abs(exponent); i++) {
            result *= base;
        }
        if (exponent >= 0) {
            return result;
        }
        return 1 / result;
    }
}
