package com.step.jdbc.runtime.flink.process;

import javax.script.*;

public class ScriptingWithJavaObject {
    public static void main(String[] args) throws ScriptException {
        // 创建 ScriptEngine 和 Binding 对象
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        Bindings binding = new SimpleBindings();
        engine.setBindings(binding, ScriptContext.ENGINE_SCOPE);

        // 在 Java 中创建一个 Person 对象，并将其传递给脚本
        Person person = new Person("John", 30);
        binding.put("person", person);

        // 在脚本中使用传递的 Java 对象
        engine.eval("print('Name: ' + person.getName());");
        engine.eval("print('Age: ' + person.getAge());");
    }

    public static class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
