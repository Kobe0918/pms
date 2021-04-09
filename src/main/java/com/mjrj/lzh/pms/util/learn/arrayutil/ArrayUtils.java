package com.mjrj.lzh.pms.util.learn.arrayutil;


import java.lang.reflect.Array;

public class ArrayUtils {
//    1.判断是否为空
//    2.兼容多种数据类型


    //判断数组是否为空  true 不为空  false 为空
    public static <T> boolean isNotEmpty(final T[] array){
       return !isEmpty(array);
    }

    public static boolean isEmpty(final Object[] array){
        return getLength(array) == 0;
    }

    public static int getLength(final Object array){
       if(array == null){
           return 0;
       }
       return Array.getLength(array);
    }


    //合并数组
    public static <T> T[] addAll(final T[] array1, final T... array2){
      if(array1 == null){
          return clone(array2);//深拷贝和浅拷贝
      }else if(array2 == null ){
          return clone(array1);
      }

      //获取数组类型
      final Class<?> type1 = array1.getClass().getComponentType();
      //创建最终合并的数组
      final T[] joinedArray = (T[])Array.newInstance(type1,array1.length + array2.length);
      //拷贝数组
      System.arraycopy(array1,0,joinedArray,0,array1.length);
      try {
          System.arraycopy(array2,0,joinedArray,array1.length,array2.length);
      } catch(final ArrayStoreException ase) {
          //报错的话判断是否由于数组类型不一致
          final Class<?> type2 = array2.getClass().getComponentType();
          if(!type1.isAssignableFrom(type2)){
              //数组类型不一致多抛出这个异常
              throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of "
                      + type1.getName(), ase);
          }
          //最终都会抛出这个异常
          throw ase;
      }
      return joinedArray;
    }

    public static <T> T[] clone(final T[] array){
      if(array == null){
          return null;
      }
      return array.clone();
    }

    public static void main(String[] args) {
        String[] s = {"s","1"};
        String[] i = {"1","2"};
        String[] m = addAll(s,i);
        for(String t : m){
            System.out.println(t);
        }
    }
}
