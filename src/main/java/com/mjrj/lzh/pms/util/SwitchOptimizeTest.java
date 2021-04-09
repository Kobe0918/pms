package com.mjrj.lzh.pms.util;

import com.mjrj.lzh.pms.validate.Status;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: pms
 * @BelongsPackage: com.mjrj.lzh.pms.util
 * @Author: lzh
 * @CreateTime: 2020-05-29 16:28
 * @Description: ${Description}
 */

@BenchmarkMode(Mode.AverageTime)  //测试完成时间
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 2,timeUnit = TimeUnit.SECONDS, time = 1) //预热两轮 每次1秒
@Measurement(iterations = 5,timeUnit = TimeUnit.SECONDS, time = 1) //测试五轮 每次1秒
@Fork(1) //一个线程
@State(Scope.Thread) //每个测试一个线程实例
public class SwitchOptimizeTest {

    static Integer NUM = 9;

    @Benchmark
    public void switchTest(){
        int num = 0;
        switch (NUM){
            case 1: num = 1;break;
            case 2: num = 2;break;
            case 3: num = 3;break;
            case 9: num = 9;break;
            default: num = -1;break;
        }
    }


    @Benchmark
    public void ifTest(){
        int num = 0;
        if(NUM == 1){
            num = 1;
        }else if(num == 2){
            num = 2;
        }else if(num == 3){
            num = 3;
        }else if (num ==9){
            num = 9;
        }else{
            num = -1;
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options build = new OptionsBuilder().include(SwitchOptimizeTest.class.getSimpleName())  //要导入的测试类
                .output("F:\\jmh-switch.log").build();
        new Runner(build).run();
    }
}
