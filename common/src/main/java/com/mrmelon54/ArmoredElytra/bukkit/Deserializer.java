package com.mrmelon54.ArmoredElytra.bukkit;

import net.minecraft.SharedConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.*;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.input.ReaderInputStream;
import org.unsynchronized.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.*;
import java.util.function.Function;

public class Deserializer {
    public static <T> T deserialize(String b64, Function<jdeserialize, T> next) throws IOException {
        try (InputStream inputStream = new Base64InputStream(new ReaderInputStream(new StringReader(b64)))) {
            jdeserialize j = new jdeserialize("aaaaa.dat");
            j.run(inputStream, false);
            return next.apply(j);
        }
    }

    public static ItemStack deserializeItemStack(String b64) throws IOException {
        return deserialize(b64, j -> {
            List<content> jc = j.getContent();
            for (content i : jc) {
                if (i.getType() == contenttype.INSTANCE && i instanceof instance ii) {
                    BukkitMap map1 = getMapField(ii, "map");
                    Optional<stringobj> iiType = map1.getTypedValue("type", stringobj.class);
                    if (iiType.isEmpty()) throw new RuntimeException("Missing type");
                    Item itemType = BuiltInRegistries.ITEM.get(new ResourceLocation((iiType.get().value.toLowerCase(Locale.ROOT))));
                    System.out.println(itemType);
                    Optional<instance> metaValue = map1.getTypedValue("meta", instance.class);
                    if (metaValue.isPresent()) {
                        BukkitMap map2 = getMapField(metaValue.get(), "map");
                        stringobj equalEqualValue = map2.getPresentTypedValue("==", stringobj.class);
                        if (!Objects.equals(equalEqualValue.value, "ItemMeta")) throw new RuntimeException("Missing ItemMeta type");
                        stringobj metaTypeValue = map2.getPresentTypedValue("meta-type", stringobj.class);
                        if (!Objects.equals(metaTypeValue.value, "ARMOR")) throw new RuntimeException("ItemMeta type is not ARMOR");

                        try {
                            String armorMeta = deserializeArmorMeta(map2.getPresentTypedValue("internal", stringobj.class).value);
                            System.out.println(armorMeta);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                    return new ItemStack(itemType);
                }
            }
            return null;
        });
    }

    private static String deserializeArmorMeta(String b64) throws IOException {
        return deserialize(b64, new Function<jdeserialize, String>() {
            @Override
            public String apply(jdeserialize j) {
                List<content> jc = j.getContent();
                for (content i : jc) {
                    System.out.println(i);
                }
                return null;
            }
        });
    }

    private static Optional<Object> getField(instance inst, String name) {
        for (Map.Entry<field, Object> fieldObjectEntry : inst.fielddata.get(inst.classdesc).entrySet())
            if (Objects.equals(fieldObjectEntry.getKey().name, name))
                return Optional.of(fieldObjectEntry.getValue());
        return Optional.empty();
    }

    private static <T extends content> Optional<T> getTypedField(instance inst, String name, Class<T> clazz) {
        return getField(inst, name).map(o -> clazz.isInstance(o) ? clazz.cast(o) : null);
    }

    private static BukkitMap getMapField(instance inst, @SuppressWarnings("SameParameterValue") String name) {
        Optional<instance> f = getTypedField(inst, name, instance.class);
        if (f.isEmpty()) throw new RuntimeException("Cannot get field instance");
        if (!f.get().classdesc.name.equals("com.google.common.collect.ImmutableMap$SerializedForm")) throw new RuntimeException("Not a map");
        instance oi = f.get();
        Optional<arrayobj> keys = getTypedField(oi, "keys", arrayobj.class);
        Optional<arrayobj> values = getTypedField(oi, "values", arrayobj.class);
        if (keys.isEmpty() || values.isEmpty()) throw new RuntimeException("Missing keys or values field");
        return new BukkitMap(keys.get(), values.get());
    }

    public static void main(String[] args) throws IOException {
        // TODO: remove this
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();

        ItemStack stack = deserializeItemStack("rO0ABXcEAAAAAXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFw\n" +
                "dAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFi\n" +
                "bGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVj\n" +
                "dDtMAAZ2YWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAA\n" +
                "BHQAAj09dAABdnQABHR5cGV0AARtZXRhdXEAfgAGAAAABHQAHm9yZy5idWtraXQuaW52ZW50b3J5\n" +
                "Lkl0ZW1TdGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2\n" +
                "YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAA50dAARR09MREVOX0NIRVNUUExBVEVzcQB+AABz\n" +
                "cQB+AAN1cQB+AAYAAAADcQB+AAh0AAltZXRhLXR5cGV0AAhpbnRlcm5hbHVxAH4ABgAAAAN0AAhJ\n" +
                "dGVtTWV0YXQABUFSTU9SdAA8SDRzSUFBQUFBQUFBLytOaVlPQmlFRTNPejZrc0tVcTBjZ1ZUb1FY\n" +
                "cFJZa3BxUXdNQUQzWjlZc2RBQUFB\n");
        System.out.println(stack.is(Items.GOLDEN_CHESTPLATE));
    }
}
