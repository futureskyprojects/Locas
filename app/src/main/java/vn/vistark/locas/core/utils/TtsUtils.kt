package vn.vistark.locas.core.utils

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import org.json.JSONObject
import java.util.*


class TtsUtils(val context: Context) : AsyncTask<String, Int, Uri?>() {

    lateinit var audioManager: AudioManager
    private var defaultMsg = ""
    private var previousStreamVolume = -1
    // Default TTS
    private var tts: TextToSpeech? = null

    override fun onPreExecute() {
        super.onPreExecute()
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    private fun defaultTTS(msg: String) {
        tts = TextToSpeech(context, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                // Lệnh khi thành công
                tts!!.language = Locale("vi")
                tts!!.setPitch(1.1F)
                tts!!.setSpeechRate(1.2F)
                tts!!.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onDone(utteranceId: String?) {
                        makeDefaultVolume()
                    }

                    override fun onError(utteranceId: String?) {
                        makeDefaultVolume()
                    }

                    override fun onStart(utteranceId: String?) {
                        makeMaxVolume()
                    }
                })
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //vi-vn-x-vif-local (Giọng nam Bắc - nói offline)
                    //vi-vn-x-vid-network (Giọng nam Bắc - off)
                    //vi-vn-x-vic-network (Giọng nữ Bắc - off)
                    //vi-vn-x-vif-network (Giọng nam Nam - off)
                    //vi-vn-x-vie-local (Giọng nữ Nam - off)
                    //vi-vn-x-gft-local (Giọng nữ mặc định- offline - dớ tệ hại)
                    //vi-vn-x-vic-local (Giọng nữ Bắc - offline - ngữ điệu - HAY)
                    //vi-vn-x-vid-local (Giọng nam Nam - offline - khá dở)
                    //vi-vn-x-vie-network (Giọng nữ Nam - offline - khá dở)
                    //vi-vn-x-gft-network (Giọng nữ Nam - offline - dở)
                    //vi-VN-language (Mặc định)

                    val name = "vi-vn-x-vic-local"
                    if (!tts!!.defaultVoice.name.contains(name)) {
                        for (v in tts!!.voices) {
                            if (v.name.contains(name)) {
                                tts!!.voice = v
                                break
                            }
                        }
                    }
                    tts!!.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null)
                } else {
                    tts!!.speak(msg, TextToSpeech.QUEUE_FLUSH, null)
                }


            }
        })
    }

    fun makeMaxVolume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
                previousStreamVolume =
                    audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    AudioManager.FLAG_PLAY_SOUND
                )
            }
        }
    }

    fun makeDefaultVolume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) {
                audioManager.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    previousStreamVolume,
                    AudioManager.FLAG_PLAY_SOUND
                )
            }
        }
    }

    // WaveNet TTS
    private var mediaPlayer = MediaPlayer()
    private val requestUrl = "https://texttospeech.googleapis.com/v1/text:synthesize"
    private val key = "AIzaSyAm2syIah1RqLbw38R6SCxPBuET-EZPNBA"
    private fun requestBodyBuilder(msg: String): JSONObject {
        val jsonBody = JSONObject()

        val audioConfig = JSONObject()
        audioConfig.put("audioEncoding", "LINEAR16")
        audioConfig.put("pitch", 4)
        audioConfig.put("speakingRate", 1)

        val input = JSONObject()
        input.put("text", msg)

        val voice = JSONObject()
        voice.put("languageCode", "vi-VN")
        voice.put("name", "vi-VN-Wavenet-A")

        jsonBody.put("audioConfig", audioConfig)
        jsonBody.put("input", input)
        jsonBody.put("voice", voice)

        return jsonBody
    }

    override fun doInBackground(vararg params: String?): Uri? {
        defaultMsg = params[0]!!
//        if (NetworkUtils.isNetworkConnected(context)) {
//            try {
//                val url = URL("$requestUrl?key=$key")
//                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
//                conn.requestMethod = "POST"
//                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
//                conn.setRequestProperty("Accept", "application/json")
//                conn.doOutput = true
//                conn.doInput = true
//                val os = DataOutputStream(conn.outputStream)
//                val writer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    BufferedWriter(
//                        OutputStreamWriter(os, StandardCharsets.UTF_8)
//                    )
//                } else {
//                    BufferedWriter(
//                        OutputStreamWriter(os, "UTF-8")
//                    )
//                }
//                writer.write(requestBodyBuilder(defaultMsg).toString())
//                writer.flush()
//                writer.close()
//                os.close()
//                if (conn.responseCode == HttpsURLConnection.HTTP_OK) {
//                    val response = StringBuilder()
//                    var line: String?
//                    val br = BufferedReader(InputStreamReader(conn.inputStream))
//                    while (br.readLine().also { line = it } != null) {
//                        response.append(line)
//                    }
//                    println("Request voice thành công ${conn.responseMessage} mã ${conn.responseCode}")
//                    val jsResponse = JSONObject(response.toString())
//                    // Khởi tạo đường dẫn File
//                    val voice = File("${context.cacheDir}${File.separator}voice.wav")
//                    // Xóa nếu đã tồn tại
//                    if (voice.exists())
//                        voice.delete()
//                    // Ghi nội dung mới vào file
//                    val fos = FileOutputStream(voice)
//                    fos.write(Base64.decode(jsResponse.getString("audioContent"), Base64.DEFAULT))
//                    fos.flush()
//                    fos.close()
//                    return voice.toUri()
//                } else {
//                    println("Request voice không thành công ${conn.responseMessage} mã ${conn.responseCode}")
//                }
//                conn.disconnect()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
        return null
    }

    override fun onPostExecute(result: Uri?) {
        super.onPostExecute(result)
        if (result != null) {
            // Nếu đang nói thì ngưng
            if (isTalking()) {
                stop()
            }
            // Thực hiện nói mới
            mediaPlayer = MediaPlayer.create(context, result)
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        } else {
            // Nếu đang nói thì ngưng
            if (isTalking()) {
                stop()
            }
            // Thực hiện nói mới
            defaultTTS(defaultMsg)
        }
    }

    //
    private fun isTalking(): Boolean {
        return tts?.isSpeaking ?: false || mediaPlayer.isPlaying
    }

    fun stop() {
        if (tts?.isSpeaking == true) {
            tts?.stop()
        }

        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            mediaPlayer.release()
        }
    }
}