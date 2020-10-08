package com.earsnot.rooms.model;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.earsnot.rooms.database.PersonDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Repository {
    private PersonDatabase db;
    private ExecutorService executor;
    private LiveData<List<Person>> persons;

    public Repository(Application app) {
        db = PersonDatabase.getDatabase(app.getApplicationContext());
        executor = Executors.newSingleThreadExecutor();
        persons = db.personDAO().getAll();
    }
    // only for demo purposes - always use asynch methods for network actions etc
    public Person getPerson(int uid) {
        return db.personDAO().findPerson(uid);
    }

    public void updatePerson(Person person) {
        db.personDAO().updatePerson(person);
    }

    public void addPerson(String firstName, String lastName) {
        db.personDAO().addPerson(new Person(firstName, lastName));
    }

    public Person searchForPerson(String firstName, String lastName) {
        return db.personDAO().findPerson(firstName, lastName);
    }

    public List<Person> searchForPersons(String name) {
        return db.personDAO().findPersons(name);
    }

    //async methods

    public Person getPersonAsync(final int uid) {
        Future<Person> p = executor.submit(new Callable<Person>() {
            @Override
            public Person call(){
                return db.personDAO().findPerson(uid);
            }
        });
        try {
            return p.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void updatePersonAsync(final Person person) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.personDAO().updatePerson(person);
            }
        });

    }

    public void addPersonAsync(final String firstName, final String lastName) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.personDAO().addPerson(new Person(firstName, lastName));
            }
        });
    }

    public Person searchForPersonAsync(final String firstName, final String lastName) {
        Future<Person> p = executor.submit(new Callable<Person>() {
            @Override
            public Person call() {
                return db.personDAO().findPerson(firstName, lastName);

            }
        });
        try {
            return p.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Person> searchForPersonsAsync(final String name) {
        Future<List<Person>> p = executor.submit(new Callable<List<Person>>() {
            @Override
            public List<Person> call() {
                return db.personDAO().findPersons(name);
            }
        });
        try {
            return p.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
