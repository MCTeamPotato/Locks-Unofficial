package melonslise.locks.common.addenchant;

import melonslise.locks.Locks;
import melonslise.locks.common.item.LockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.lang.reflect.*;
import java.util.Arrays;

public class EnumModifier {
    public static void run() {
        try {
            // 1. 创建新的枚举实例
            EnchantmentCategory customItem = addEnumConstant(
                    EnchantmentCategory.class,
                    "LOCKS",
                    new Class<?>[0],
                    new Object[0],
                    (Item item) -> item instanceof LockItem
            );

        } catch (Exception e) {
            Locks.LOGGER.error("Failed to add custom enchantment category", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T addEnumConstant(
            Class<T> enumClass, String name, Class<?>[] paramTypes, Object[] paramValues,
            CanEnchantChecker canEnchantChecker) throws Exception {

        Field valuesField = enumClass.getDeclaredField("$VALUES");
        valuesField.setAccessible(true);

        T[] oldValues = (T[]) valuesField.get(null);
        T[] newValues = Arrays.copyOf(oldValues, oldValues.length + 1);

        // 使用反射创建新实例，设置 canEnchant 行为
        Constructor<T> constructor = enumClass.getDeclaredConstructor(paramTypes);
        constructor.setAccessible(true);
        T newConstant = constructor.newInstance(paramValues);

        // 这里使用代理创建 canEnchant 方法的实现
        Method canEnchantMethod = enumClass.getMethod("canEnchant", Item.class);
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.equals(canEnchantMethod)) {
                return canEnchantChecker.canEnchant((Item) args[0]);
            }
            return method.invoke(newConstant, args);
        };

        T proxyInstance = (T) Proxy.newProxyInstance(
                enumClass.getClassLoader(),
                new Class<?>[]{enumClass},
                handler
        );

        newValues[newValues.length - 1] = proxyInstance;
        valuesField.set(null, newValues);

        return proxyInstance;
    }

    // 自定义接口，用于 canEnchant 检测
    interface CanEnchantChecker {
        boolean canEnchant(Item item);
    }
}
