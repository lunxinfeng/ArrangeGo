package cn.izis.util

import com.lxf.rxretrofit.callback.BaseView
import com.lxf.rxretrofit.callback.ProgressObserver
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers

object Transformer {
    fun <U> io_main() = ObservableTransformer<U,U>{
        it.subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
    }
}

fun <T> Observable<T>.sub(onNext:(data:T) -> Unit){
    this.subscribe(object : Progress<T>() {
        override fun doOnNext(data: T) {
            onNext.invoke(data)
        }
    })
}

abstract class Progress<T>(
    view:BaseView? = null
):ProgressObserver<T,BaseView>(
    view = view
) {
    override fun doOnError(errorMessage: String) {
        toast(errorMessage)
        warning(errorMessage)
    }
}