package cn.wxjia.gfmonitor.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cn.wxjia.gfmonitor.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateManager {
	private static final String TAG = "VersionService";
	private static final int DOWNLOADING = 1;
	private static final int DOWNLOAD_FINISH = 2;
	HashMap<String, String> mHashMap;
	/* ���ر���·�� */
	private String mSavePath;
	/* ��¼���������� */
	private int progress;
	/* �Ƿ�ȡ������ */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* ���½����� */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOADING:
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	public void checkUpdate() {
		if (isUpdate()) {
			showNoticeDialog();
		} else {
			Toast.makeText(mContext, "�������", Toast.LENGTH_LONG).show();
		}
	}

	private boolean isUpdate() {
		VersionService versionService = new VersionService();
		// ��ȡ��ǰ����汾
		int versionCode = versionService.getVersionCode(mContext);
		// version.xml��·��
		String versionXmlPath = "http://www.vcyoung.cn/tecentWeb/xml/version.xml";

		InputStream inStream = new XmlService()
				.getXmlImputStream(versionXmlPath);
		// ����XML�ļ��� ����XML�ļ��Ƚ�С�����ʹ��DOM��ʽ���н���
		VersionService service = new VersionService();
		try {
			mHashMap = service.parseXml(inStream);
		} catch (Exception e) {
			Log.i(TAG, "Exception = " + e.getMessage());
		}
		if (null != mHashMap) {
			int serviceCode = Integer.valueOf(mHashMap.get("version"));
			// �汾�ж�
			if (serviceCode > versionCode) {
				return true;
			}
		}
		return false;
	}

	private void showNoticeDialog() {
		// ����Ի���
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("APP����");
		builder.setMessage("APP����");

		builder.setPositiveButton("���ڸ���", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// ��ʾ���ضԻ���
				showDownloadDialog();
			}
		});
		// �Ժ����
		builder.setNegativeButton("�Ժ����", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		try {
			noticeDialog.show();
		} catch (Exception e) {
			Log.i(TAG, "noticeDialog.show() = " + e.getMessage());
		}
	}

	/**
	 * ��ʾ������ضԻ���
	 */
	private void showDownloadDialog() {
		// ����������ضԻ���
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("APP����");
		// �����ضԻ������ӽ�����
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// ȡ������
		builder.setNegativeButton("ȡ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// ����ȡ��״̬
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// �����ļ�
		downloadApk();
	}

	/**
	 * ����apk�ļ�
	 */
	private void downloadApk() {
		// �������߳��������
		try {
			new downloadApkThread().start();
		} catch (Exception e) {
			Log.i(TAG, "�߳��쳣 = " + e.getMessage());
		}
	}

	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			try {
				// �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// ��ô洢����·��
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// ��������
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();
					// ��ȡ�ļ���С
					int length = conn.getContentLength();
					// ����������
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// �ж��ļ�Ŀ¼�Ƿ����
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// ����
					byte buf[] = new byte[1024];
					// д�뵽�ļ���
					do {
						int numread = is.read(buf);
						count += numread;
						// ���������λ��
						progress = (int) (((float) count / length) * 100);
						// ���½���
						mHandler.sendEmptyMessage(DOWNLOADING);
						if (numread <= 0) {
							// �������
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// д���ļ�
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// ���ȡ����ֹͣ����.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				Log.i(TAG, "MalformedURLException = " + e.getMessage());
			} catch (IOException e) {
				Log.i(TAG, "IOException = " + e.getMessage());
			}
			// ȡ�����ضԻ�����ʾ
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * ��װAPK�ļ�
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists()) {
			return;
		}
		// ͨ��Intent��װAPK�ļ�
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
