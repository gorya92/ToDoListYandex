package com.android.kotlinmvvmtodolist.usecases

import com.android.kotlinmvvmtodolist.repository.RetrofitRepository
import com.android.kotlinmvvmtodolist.retrofitConnect.RetrofitConstants

class GetRevisionUseCase() {
    private val retrofitRepository: RetrofitRepository
    init {
        retrofitRepository = RetrofitRepository()
    }
    suspend fun getRevision(){
        val response = retrofitRepository.getList()
        RetrofitConstants.REVISION = response.body()!!.revision
    }
}