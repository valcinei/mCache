package wiki.depasquale.mcache.adapters;

import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import wiki.depasquale.mcache.core.FileMap;
import wiki.depasquale.mcache.core.FileParams;
import wiki.depasquale.mcache.core.IOHandler;

public final class FilesIOHandler implements IOHandler {

  public FilesIOHandler() {}

  @Override
  @NonNull
  public final <T> Observable<T> get(Class<T> cls, @NonNull FileParams params) {
    FileMap<T> fileMap = FileMap.forFolder(params.getFileClass(), false);
    if (fileMap != null) {
      return fileMap.matching(params)
          .flatMapIterable(it -> it)
          .flatMap(fileMap::getObject)
          .observeOn(AndroidSchedulers.mainThread());
    }
    PublishSubject<T> empty = PublishSubject.create();
    return empty.doOnSubscribe(disposable -> empty.onError(new Throwable("No file was found.")));
  }

  @Override
  public final <T> void save(@NonNull T object, @NonNull FileParams params) {
    FileMap<T> fileMap = FileMap.forFolder(params.getFileClass(), false);
    if (fileMap != null) {
      Log.d("RxU", "saving...");
      fileMap.save(object, params);
    } else {
      Log.d("RxU", "Map is null, the hell?");
    }
  }

  @Override
  public final void clean() {
    /*Context context = MCache.get();
    if (context == null) {
      return;
    }
    for (String file : context.fileList()) {
      if (file.startsWith(MCache.sPrefix)) {
        context.deleteFile(file);
      }
    }*/
  }
}
