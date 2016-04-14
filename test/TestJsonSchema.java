import bean.SimpleBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;

public class TestJsonSchema {

    public static void main(String[] args) throws Exception {
        ObjectMapper MAPPER = new ObjectMapper();
        JsonSchemaGenerator generator = new JsonSchemaGenerator(MAPPER);
        JsonSchema jsonSchema = generator.generateSchema(SimpleBean.class);
        String mapSchemaStr = MAPPER.writeValueAsString(jsonSchema);
        System.out.println(mapSchemaStr);

        ObjectMapper m = new ObjectMapper();
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        m.acceptJsonFormatVisitor(m.constructType(SimpleBean.class), visitor);
        JsonSchema jsonSchema2 = visitor.finalSchema();
        String mapSchemaStr2 = MAPPER.writeValueAsString(jsonSchema2);
        System.out.println(mapSchemaStr2);

    }

    public static void valid() {
        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonValidator v = factory.getValidator();
        // 不合用
        // v.validate(schema, instance);
    }

}
