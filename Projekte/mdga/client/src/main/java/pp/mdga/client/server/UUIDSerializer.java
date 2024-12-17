package pp.mdga.client.server;

import com.jme3.network.serializing.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Serializer for UUID objects to be used with jME3 networking.
 */
public class UUIDSerializer extends Serializer {

    /**
     * Reads a UUID object from the given ByteBuffer.
     *
     * @param data the ByteBuffer containing the serialized UUID
     * @param c the class type of the object to be read
     * @param <T> the type of the object to be read
     * @return the deserialized UUID object, or null if the UUID is a placeholder
     * @throws IOException if an I/O error occurs
     */
    @Override
    public <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
        byte[] uuid = new byte[36];
        data.get(uuid);

        if (uuid.equals(new UUID(1, 1))) {
            return null;
        } else {
            return (T) UUID.fromString(new String(uuid));
        }
    }

    /**
     * Writes a UUID object to the given ByteBuffer.
     *
     * @param buffer the ByteBuffer to write the serialized UUID to
     * @param object the UUID object to be serialized
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        UUID uuid = (UUID) object;

        if (uuid != null) {
            buffer.put(uuid.toString().getBytes());
        } else {
            buffer.put(new UUID(1, 1).toString().getBytes());
        }
    }
}
