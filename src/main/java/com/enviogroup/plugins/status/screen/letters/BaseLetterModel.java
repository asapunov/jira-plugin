package com.enviogroup.plugins.status.screen.letters;

import com.enviogroup.plugins.status.screen.IssueModel;

import java.util.LinkedList;
import java.util.List;

public class BaseLetterModel extends IssueModel {


    public BaseLetterModel() {

    }


    public BaseLetterModel(BaseLetterModel that) {
        this.childLetter = that.getChildLetter();
        this.parentLetter = that.getParentLetter();
        this.letterType = that.getLetterType();
        this.setKey(that.getKey());
        this.setSummary(that.getSummary());
        this.setStatus(that.getStatus());
    }

    private String letterType;

    public String getLetterType() {
        return letterType;
    }

    public void setLetterType(String letterType) {
        this.letterType = letterType;
    }

    private BaseLetterModel childLetter;

    private BaseLetterModel parentLetter;

    public BaseLetterModel getChildLetter() {
        return childLetter;
    }

    public void setChildLetter(BaseLetterModel childLetter) {
        this.childLetter = childLetter;
    }

    public BaseLetterModel getParentLetter() {
        return parentLetter;
    }

    public void setParentLetter(BaseLetterModel parentLetter) {
        this.parentLetter = parentLetter;
    }

    public int getChildCount() {
        BaseLetterModel letterModel = this;
        int i = 0;
        while (letterModel.getChildLetter() != null) {
            letterModel = letterModel.getChildLetter();
            i++;
        }
        return i;
    }

    public int getParentCount() {
        BaseLetterModel letterModel = this;
        int i = 0;
        while (letterModel.getParentLetter() != null) {
            letterModel = letterModel.getParentLetter();
            i++;
        }
        return i;
    }

    public int getSize() {
        return getChildCount() + getParentCount();
    }

    public BaseLetterModel getFirstParent() {
        BaseLetterModel letterModel = this;
        while (letterModel.getParentLetter() != null) {
            letterModel = letterModel.getParentLetter();
        }
        return letterModel;
    }

    public BaseLetterModel getLastChild() {
        BaseLetterModel letterModel = this;
        while (letterModel.getChildLetter() != null) {
            letterModel = letterModel.getChildLetter();
        }
        return letterModel;
    }

//    LinkedList<BaseLetterModel> letterChain = new LinkedList<>();
//    public List<BaseLetterModel> getChainAsLinkedList() {
//        BaseLetterModel letterModel = this;
//
//        letterChain.add(letterModel);
//        while (letterModel.getChildLetter() != null)
//    }
}
