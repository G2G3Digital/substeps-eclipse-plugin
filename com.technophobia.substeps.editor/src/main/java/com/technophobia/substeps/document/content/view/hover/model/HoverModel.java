package com.technophobia.substeps.document.content.view.hover.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class HoverModel {

    private static final String SEPARATOR = "%%%";
    private final String header;
    private final String body;
    private final String location;


    public HoverModel(final String header, final String body, final String location) {
        this.header = header;
        this.body = body;
        this.location = location;
    }


    public String header() {
        return header;
    }


    public String body() {
        return body;
    }


    public String location() {
        return location;
    }


    public static HoverModel fromString(final String s) {
        final String[] split = s.split(SEPARATOR);
        if (split.length != 4) {
            throw new IllegalStateException("Expected 4 elements in string " + s + ". Instead found " + split.length);
        }

        final String model = split[0];
        final String header = split[1];
        final String body = split[2];
        final String location = split[3];

        return instantiate(valueFrom(model), valueFrom(header), valueFrom(body), valueFrom(location));
    }


    public String serializeToString() {
        final StringBuilder sb = new StringBuilder();
        value("modelClass", this.getClass().getName(), sb);
        value("header", header, sb);
        value("body", body, sb);
        value("location", location, sb);

        return sb.toString();
    }


    private void value(final String type, final String value, final StringBuilder sb) {
        sb.append(type);
        sb.append("=");
        sb.append(value);
        sb.append(SEPARATOR);
    }


    private static String valueFrom(final String line) {
        return line.substring(line.indexOf('=') + 1);
    }


    @SuppressWarnings("unchecked")
    private static HoverModel instantiate(final String model, final String header, final String body,
            final String location) {
        try {
            final Constructor<HoverModel> cons = (Constructor<HoverModel>) Class.forName(model).getConstructor(
                    String.class, String.class, String.class);
            return cons.newInstance(header, body, location);
        } catch (final NoSuchMethodException ex) {
            throw new IllegalStateException("Could not construct model " + model, ex);
        } catch (final SecurityException ex) {
            throw new IllegalStateException("Could not construct model " + model, ex);
        } catch (final ClassNotFoundException ex) {
            throw new IllegalStateException("Could not construct model " + model, ex);
        } catch (final InstantiationException ex) {
            throw new IllegalStateException("Could not construct model " + model, ex);
        } catch (final IllegalAccessException ex) {
            throw new IllegalStateException("Could not construct model " + model, ex);
        } catch (final IllegalArgumentException ex) {
            throw new IllegalStateException("Could not construct model " + model, ex);
        } catch (final InvocationTargetException ex) {
            throw new IllegalStateException("Could not construct model " + model, ex);
        }
    }
}
