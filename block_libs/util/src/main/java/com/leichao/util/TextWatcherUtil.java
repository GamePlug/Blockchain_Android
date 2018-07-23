package com.leichao.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextWatcherUtil {

    /**
     * 简化文字变化监听器
     */
    public static class Simple implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 简化EditText输入监听器,onFilter对文字过滤
     * 只要重写onChange即可，解决重写TextWatcher容易发生的死循环问题，光标问题，无法连续删除问题
     */
    public abstract class Filter extends Simple {

        protected EditText mEditText;
        protected boolean isChange = true;

        public Filter(EditText editText) {
            this.mEditText = editText;
        }

        @Override
        public final void afterTextChanged(Editable editable) {
            if (isChange) {
                isChange = false;

                String text = mEditText.getText().toString();
                int length = text.length();
                int start = mEditText.getSelectionStart();

                text = onFilter(text);
                if (!text.equals(mEditText.getText().toString())) {
                    mEditText.setText(text);
                }

                int sublength = length - text.length();// 截取的长度
                int selection = start - sublength;// 光标位置
                try {
                    mEditText.setSelection(selection);
                } catch (Exception e) {
                }

                afterTextChanged(text);

                isChange = true;
            }
        }

        // 必须重写，在此方法中实现你对文字的修改
        protected abstract String onFilter(String text);

        protected void afterTextChanged(String text) {

        }
    }

    /**
     * 自定义字符过滤的TextWatcher
     * 可以指定中文英文或数字的任意组合，以及指定额外的字符，或者正则表达式
     */
    public class Regex extends Filter {

        public static final int CHINESE = 1;
        public static final int ENGLISH = 2;
        public static final int NUMBER = 4;

        public static final String chReg = "\u4E00-\u9FA5";
        public static final String enReg = "a-zA-Z";
        public static final String nuReg = "0-9";

        private String mRegex = "";
        private boolean isFilterEmoji = false;

        /**
         * 构造方法
         * @param editText EditText控件
         */
        public Regex(EditText editText) {
            super(editText);
        }

        /**
         * 包含的字符
         * @param type CHINESE|ENGLISH|NUMBER三者任意组合
         */
        public Regex containChar(int type) {
            return containChar(type, "");
        }

        /**
         * 包含的字符
         * @param type CHINESE|ENGLISH|NUMBER三者任意组合
         * @param charStr 可以输入的字符，如：",.:;\"，。：；“”"
         */
        public Regex containChar(int type, String charStr) {
            String regex = "";
            if (type == CHINESE) {// 中文
                regex = chReg + charStr;
            } else if (type == ENGLISH) {// 英文
                regex = enReg + charStr;
            } else if (type == (CHINESE|ENGLISH)) {// 中文或英文
                regex = chReg + enReg + charStr;
            } else if (type == NUMBER) {// 数字
                regex = nuReg + charStr;
            } else if (type == (CHINESE|NUMBER)) {// 中文或数字
                regex = chReg + nuReg + charStr;
            } else if (type == (ENGLISH|NUMBER)) {// 英文或数字
                regex = enReg + nuReg + charStr;
            } else if (type == (CHINESE|ENGLISH|NUMBER)) {// 中文英文或数字
                regex = chReg + enReg + nuReg + charStr;
            }
            mRegex = "[^" + regex + "]";
            return this;
        }

        /**
         * 过滤的字符
         * @param regex 字符串或正则表达式，该字符串或符合该正则表达式的字符串将被删除
         */
        public Regex filterRegex(String regex) {
            mRegex = regex;
            return this;
        }

        /**
         * 过滤表情
         */
        public Regex filterEmoji() {
            isFilterEmoji = true;
            return this;
        }

        @Override
        protected final String onFilter(String text) {
            if (isFilterEmoji) {
                text = TextUtil.filterEmoji(text);
            }
            return text.replaceAll(mRegex, "");
        }
    }

}
