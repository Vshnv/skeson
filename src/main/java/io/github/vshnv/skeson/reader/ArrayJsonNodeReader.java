package io.github.vshnv.skeson.reader;

import io.github.vshnv.skeson.parse.IndexedString;
import io.github.vshnv.skeson.JsonNode;
import io.github.vshnv.skeson.branching.ArrayJsonNode;

import java.util.ArrayList;
import java.util.List;

import static io.github.vshnv.skeson.parse.ParsingUtils.parseAssert;
import static io.github.vshnv.skeson.parse.ParsingUtils.skipSpaces;

public final class ArrayJsonNodeReader implements JsonNodeReader<ArrayJsonNode> {
    private final JsonNodeReaderRegistry nodeReaderRegistry;

    public ArrayJsonNodeReader(final JsonNodeReaderRegistry nodeReaderRegistry) {
        this.nodeReaderRegistry = nodeReaderRegistry;
    }

    @Override
    public ArrayJsonNode read(final IndexedString input) {
        final List<JsonNode> nodes = new ArrayList<>();
        skipSpaces(input);
        parseAssert(input.getCharAtIndex() == '[');
        input.incrementIndex();
        final JsonNodeReader<JsonNode> reader = nodeReaderRegistry.getReader(JsonNode.class);
        while (input.getCharAtIndex() != ']') {
            nodes.add(reader.read(input));
            skipSpaces(input);
            if (input.getCharAtIndex() == ',') {
                input.incrementIndex();
            } else {
                break;
            }
            skipSpaces(input);
        }
        skipSpaces(input);
        parseAssert(input.getCharAtIndex() == ']');
        input.incrementIndex();
        return new ArrayJsonNode(nodes);
    }
}
