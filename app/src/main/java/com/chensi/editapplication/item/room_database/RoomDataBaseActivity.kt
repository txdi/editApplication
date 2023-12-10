package com.chensi.editapplication.item.room_database

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chensi.editapplication.databinding.ActivityRoomDatabaseBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.SimpleTimeZone
import java.util.UUID

class RoomDataBaseActivity : AppCompatActivity() {

    private val mBinding: ActivityRoomDatabaseBinding by lazy {
        ActivityRoomDatabaseBinding.inflate(layoutInflater)
    }

    private var db: AppRoomDatabase? = null

    private var dao: UserDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        db = Room.databaseBuilder(this, AppRoomDatabase::class.java, "dataUser").build()
        dao = db?.userDao()

        mBinding.tvAdd.setOnClickListener {
            val uuid = UUID.randomUUID()
            dao?.addUser(
                UserBean(
                    uid = uuid.toString(),
                    name = uuid.toString(),
                    date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
                )
            )
        }

        mBinding.tvReload.setOnClickListener {
            val list = dao?.getAllData()
            mBinding.tvShow.text = ""
            list?.let {
                val stringBuilder = StringBuilder()
                for (userBean in it) {
                    stringBuilder.append(userBean.uid)
                    stringBuilder.append("::")
                    stringBuilder.append(userBean.name)
                    stringBuilder.append("::")
                    stringBuilder.append(userBean.date)
                    stringBuilder.append("\n")
                }
                mBinding.tvShow.text = stringBuilder.toString()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }

}