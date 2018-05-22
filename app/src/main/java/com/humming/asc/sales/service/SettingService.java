package com.humming.asc.sales.service;

import android.util.Xml;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SettingService implements ISettingService {
    private List<String> brandList = null;
    private List<String> categoryNameList = null;
    private List<String> itemcodeList = null;
    private static ObjectMapper mapper;


    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * com.humming.asc.dp.android.service.ISettingService#getItemcodeList(com
     * .humming.asc.dp.android.service.IDataReadyCallback)
     */
    @Override
    public void getItemcodeList(
            IDataReadyCallback<List<String>> dataReadyCallback) {
        if (itemcodeList != null) {
            dataReadyCallback.onDataReady(itemcodeList, null);
        } else {
            getList(dataReadyCallback, Config.ITEM_CODE_DOWNLOAD_PATH,
                    Config.ITEM_CODE_STORAGE_PATH);
        }
    }

    private void getList(
            final IDataReadyCallback<List<String>> dataReadyCallback,
            final String downloadPath, final String storagePath) {
        try {
            parse(dataReadyCallback, storagePath);
        } catch (FileNotFoundException e) {
            String url = "http://" + Config.SERVER + "/" + downloadPath;
            DownloadTask task = new DownloadTask(Application.getInstance().getCurrentActivity(), url,
                    storagePath, new IDataReadyCallback<Void>() {
                @Override
                public void onDataReady(Void result,
                                        RESTException restException) {
                    if (restException == null) {
                        try {
                            parse(dataReadyCallback, storagePath);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        dataReadyCallback.onDataReady(null,
                                restException);
                    }
                }
            });
            task.execute();
        }
    }

    private void parse(
            final IDataReadyCallback<List<String>> dataReadyCallback,
            String storagePath) throws FileNotFoundException {
        FileInputStream fis = Application.getInstance().getCurrentActivity().openFileInput(
                storagePath);
        StringListXmlParser parser = new StringListXmlParser();
        List<String> list = null;
        try {
            list = parser.parse(fis);
            dataReadyCallback.onDataReady(list, null);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class StringListXmlParser {
        // We don't use namespaces
        private final String ns = null;

        public List<String> parse(InputStream in)
                throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
                        false);
                parser.setInput(in, null);
                parser.nextTag();
                parser.nextTag();
                return readArray(parser);
            } finally {
                in.close();
            }
        }

        private List<String> readArray(XmlPullParser parser)
                throws XmlPullParserException, IOException {
            List<String> array = new ArrayList<String>();
            parser.require(XmlPullParser.START_TAG, ns, "array");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                // Starts by looking for the entry tag
                if (name.equals("string")) {
                    array.add(readString(parser));
                }
            }
            return array;
        }

        private String readString(XmlPullParser parser) throws IOException,
                XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, "string");
            String text = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "string");
            return text;
        }

        private String readText(XmlPullParser parser) throws IOException,
                XmlPullParserException {
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }
    }

    @Override
    public void getCategoryNameList(
            IDataReadyCallback<List<String>> dataReadyCallback) {
        if (categoryNameList != null) {
            dataReadyCallback.onDataReady(categoryNameList, null);
        }
        getList(dataReadyCallback, Config.CATEGORY_NAME_JSON_DOWNLOAD_PATH,
                Config.CATEGORY_NAME_JSON_STORAGE_PATH);
    }

    @Override
    public void getBrandList(IDataReadyCallback<List<String>> dataReadyCallback) {
        if (brandList != null) {
            dataReadyCallback.onDataReady(brandList, null);
        } else {
            getList(dataReadyCallback, Config.BRAND_DOWNLOAD_PATH,
                    Config.BRAND_STORAGE_PATH);
        }

    }
}
