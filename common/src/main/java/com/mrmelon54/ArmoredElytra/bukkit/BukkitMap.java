package com.mrmelon54.ArmoredElytra.bukkit;

import org.unsynchronized.arrayobj;
import org.unsynchronized.content;
import org.unsynchronized.stringobj;

import java.util.Objects;
import java.util.Optional;

public class BukkitMap {
    final arrayobj keys;
    final arrayobj values;

    public BukkitMap(arrayobj keys, arrayobj values) {
        this.keys = keys;
        this.values = values;
    }

    public Optional<Object> getValue(String name) {
        for (int i = 0; i < keys.data.size(); i++) {
            String key = ((stringobj) keys.data.get(i)).value;
            if (Objects.equals(key, name)) return Optional.ofNullable(values.data.get(i));
        }
        return Optional.empty();
    }

    public <T extends content> Optional<T> getTypedValue(String name, Class<T> clazz) {
        return getValue(name).map(o -> clazz.isInstance(o) ? clazz.cast(o) : null);
    }

    public <T extends content> T getPresentTypedValue(String name, Class<T> clazz) {
        return getTypedValue(name, clazz).orElseThrow();
    }
}
