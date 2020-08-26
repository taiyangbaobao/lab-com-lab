package decorate;

/**
 * 1 组件类 componet  包装类 decorater
 * 2 decorater 实现 componet
 * 3 decorater 聚合 componet 可以动态添加方法
 */
public abstract class ABattercake {
    protected abstract String getDesc();
    protected abstract int cost();
}
