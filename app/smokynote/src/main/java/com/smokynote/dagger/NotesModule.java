package com.smokynote.dagger;

import com.smokynote.NotesListFragment;
import com.smokynote.note.NotesRepository;
import com.smokynote.note.impl.NotesRepositoryImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * @author Maksim Zakharov
 * @author $Author$ (current maintainer)
 * @since 1.0
 */
@Module(injects = {
        NotesListFragment.class
})
public class NotesModule {

    @Provides
    @Singleton
    public NotesRepository provideNotesRepository() {
        return new NotesRepositoryImpl();
    }
}
