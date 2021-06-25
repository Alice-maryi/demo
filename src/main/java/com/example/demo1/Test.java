package com.example.demo1;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.*;

/**
 * @Author: Y.C
 * @Date: 2021/6/7 1:30 下午
 */
public class Test {

    @org.junit.Test
    public void test(){
        Integer i = 5;
        Integer i2 = null;
    }

    @org.junit.Test
    public void col() throws JsonProcessingException {
//        Stack stack = new Stack();
//        for (int i = 0; i < 10; i++) {
//            stack.push(i);
//        }
//        while (stack.length() > 0) {
//            System.out.println(stack.pop());
//        }
//
//        LinkNode node = new LinkNode(1, new LinkNode(2, new LinkNode(3, new LinkNode(4, new LinkNode(5)))));
//        node.insert(new LinkNode(5), 2);
//        node.delete(2);

//        TreeNode tree = new TreeNode(50, new TreeNode(30, new TreeNode(15), new TreeNode(45)), new TreeNode(60, new TreeNode(55), new TreeNode(70)));
//        bl(tree);
        int[] numbers = new int[]{2,7,11,15};
        Arrays.sort(numbers);
        int target = 12;
        int i = 0, j = 0;
        while (i == j || numbers[i] + numbers[j] != target) {
            if (numbers[i] + numbers[j] > target) {i++; j = i + 1;}
            else j++;
            if (j / (numbers.length) == 1) {i++; j %= (numbers.length);}
            if(i == numbers.length - 2 && j == numbers.length - 1) break;
        }
        System.out.println(i + "  " + j);

    }


    static int str(String str1, String str2) {
        char[] cs = str2.toCharArray();
        int[] is = new int[str2.length()];
        is[0] = -1;


        return -1;
    }

    static TreeNode bl(TreeNode treeNode) {
        if (treeNode.leftNode != null) return bl(treeNode.leftNode);
        System.out.println(treeNode.value);
        if (treeNode.rightNode != null) return bl(treeNode.rightNode);
        return null;
    }

    class TreeNode{
        int value = -1;
        TreeNode leftNode;
        TreeNode rightNode;

        public TreeNode() {
        }

        public TreeNode(int value) {
            this.value = value;
        }

        public TreeNode(int value, TreeNode leftNode, TreeNode rightNode) {
            this.value = value;
            this.leftNode = leftNode;
            this.rightNode = rightNode;
        }

        public void insert(TreeNode node) {
            TreeNode node1 = this;
            while (node1 != null && node1.value != -1) {
                if (node.value >= node1.value) {
                    if (node1.rightNode == null) {
                        node1.rightNode = node;
                        break;
                    }
                    node1 = node1.rightNode;
                } else {
                    if (node1.leftNode == null) {
                        node1.leftNode = node;
                        break;
                    }
                    node1 = node1.leftNode;
                }
            }
        }
    }

    class LinkNode{
        private LinkNode next;
        private int value;

        LinkNode(){

        }
        LinkNode(int value, LinkNode linkNode){
            this.value = value;
            this.next = linkNode;
        }

        LinkNode(int value){
            this.value = value;
        }

        public void delete(int index) {
            if (index < 0) return;
            LinkNode node = new LinkNode();
            for (int i = 0; i < index - 1; i++) {
                node = next;
            }
            node.next = node.next.next;
        }

        public void insert(LinkNode node, int index) {
            if (index < 0) return;
            LinkNode node1 = new LinkNode();
            for (int i = 0; i < index - 1; i++) {
                node1 = next;
            }
            node.next = node1.next;
            node1.next = node;

        }


        public LinkNode next() {
            return next;
        }

        public int value(){
            return value;
        }
    }

    class Stack{
        private int[] array = new int[20];
        private int top = -1;
        private int len = 0;

        public void push(int i){
            array[++top] = i;
            len++;
        }

        public int pop() {
            len--;
            return array[top--];
        }

        public int length(){
            return len;
        }

    }



}
