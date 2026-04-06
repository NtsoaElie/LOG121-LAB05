package com.example.lab5.controller;

public interface Command {
    void execute();
    void undo();
}