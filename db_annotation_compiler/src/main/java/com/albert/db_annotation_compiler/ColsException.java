package com.albert.db_annotation_compiler;

import javax.lang.model.element.Element;

public class ColsException extends Exception{
    private Element mElement;



    public ColsException(Element element,String s) {
        super(s);
        mElement = element;
    }

    public Element getElement() {
        return mElement;
    }
}
