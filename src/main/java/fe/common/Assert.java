package fe.common;

import fe.common.exception.ParamException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by fe on 2017/1/18.
 */
public class Assert {
    public static void assertIsMinus(Integer i,String msg) {
        if (i.compareTo(0) == -1) throw new ParamException(ErrorCodeConstants.PARAM_ERROR,msg);
    }

    public static void assertIsMinusOrZero(Integer i,String msg) {
        if (i.compareTo(0) == -1 || i.compareTo(0) == 0) throw new ParamException(ErrorCodeConstants.PARAM_ERROR,msg);
    }

    public static void assertNotNull(Object param) {
        Assert.assertNotNull(Arrays.asList(param), ErrorCodeConstants.PARAM_ERROR,"参数缺失!");
    }

    public static void assertNotNull(Object param,String msg) {
        Assert.assertNotNull(Arrays.asList(param), ErrorCodeConstants.PARAM_ERROR,msg);
    }

    public static void assertNotEquals(String paramOne,String paramTwo,String msg) {
        if (!StringUtils.equals(paramOne,paramTwo)) throw new ParamException(ErrorCodeConstants.PARAM_ERROR,msg);
    }

    public static void assertNotNull(Object param,String code,String msg) {
        Assert.assertNotNull(Arrays.asList(param),code,msg);
    }

    public static void assertNotNull(List<Object> paramList) {
        assertNotNull(paramList, ErrorCodeConstants.PARAM_ERROR, "参数缺失!");
    }

    public static void assertNotNull(List<Object> paramList,String msg) {
        assertNotNull(paramList, ErrorCodeConstants.PARAM_ERROR, msg);
    }

    public static void assertNotNull(List<Object> paramList,String code,String msg) {
        Optional.ofNullable(paramList).ifPresent(list -> {
            list.forEach(item -> {
                if (item instanceof String) {
                    String str = (String)item;
                    if (StringUtils.isEmpty(str)) throw new ParamException(code,msg);
                } else if (item instanceof Collection) {
                    Collection<?> collections = (Collection<?>)item;
                    if (CollectionUtils.isEmpty(collections)) throw new ParamException(code,msg);
                } else {
                    Optional.ofNullable(item).orElseThrow(() -> {
                        return new ParamException(code,msg);
                    });
                }
            });
        });
    }
}
