package mx.lcv.beaconslista

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker
import com.estimote.coresdk.observation.region.beacon.BeaconRegion
import com.estimote.coresdk.recognition.packets.Beacon
import com.estimote.coresdk.service.BeaconManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var admBeacons: BeaconManager? = null
    private var region : BeaconRegion? = null

    private var arrBeacons : MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //Beacons
        admBeacons = BeaconManager(this)
        region = BeaconRegion("MisBeacons", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null)

        admBeacons?.setRangingListener {
            beaconRegion: BeaconRegion?, beacons: MutableList<Beacon>? ->
            println("Beacons detectados, $beacons")
            if (arrBeacons == null){
                arrBeacons = MutableList<String>(0, {""})
            }
            arrBeacons?.clear()
            val iterador = beacons?.iterator()!!
            for (beacon: Beacon in iterador){
                val strBeacon = "M: ${beacon.major}, m: ${beacon.minor}, rss: ${beacon.rssi}"
                arrBeacons?.add(strBeacon)
            }
            // poner el arr en el listView
            val arrDatos = arrBeacons as ArrayList<String>
            val adaptador = ArrayAdapter<String>(baseContext, android.R.layout.simple_expandable_list_item_1, arrDatos)
            listaBeacons.adapter = adaptador
        }



        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onResume() {
        super.onResume()
        SystemRequirementsChecker.checkWithDefaultDialogs(this)
        admBeacons?.connect {
            admBeacons?.startRanging(region)
        }
    }

    override fun onStop() {
        admBeacons?.stopRanging(region)
        super.onStop()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
