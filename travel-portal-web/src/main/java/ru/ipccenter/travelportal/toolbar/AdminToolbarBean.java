package ru.ipccenter.travelportal.toolbar;

import ru.ipccenter.travelportal.dialogs.CityBean;
import ru.ipccenter.travelportal.dialogs.CountryBean;
import ru.ipccenter.travelportal.dialogs.CustomerBean;
import ru.ipccenter.travelportal.dialogs.OfficeBean;
import ru.ipccenter.travelportal.utils.PopupBuilder;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ivan on 19.05.2015.
 */
@RequestScoped
@ManagedBean
public class AdminToolbarBean {

    public void openCreateCountry() {
        new PopupBuilder() {
            @Override
            protected Map<String, Object> getPopupOptions() {
                return new HashMap<String, Object>() {{
                    put("modal", true);
                    put("draggable", false);
                    put("resizable", false);
                    put("contentWidth", 380);
                    put("contentHeight", 220);
                    put("includeViewParams", true);
                }};
            }

            @Override
            protected Map<String, List<String>> getPopupParams() {
                return new HashMap<String, List<String>>() {{
                    put(CountryBean.COUNTRY_ID_KEY, Collections.singletonList(""));
                }};
            }

            @Override
            protected String getOutcome() {
                return "/dialogs/country.xhtml";
            }
        }.show();
    }

    public void openShowCountries() {
        new PopupBuilder() {
            @Override
            protected Map<String, Object> getPopupOptions() {
                return new HashMap<String, Object>() {{
                    put("modal", true);
                    put("draggable", false);
                    put("resizable", false);
                    put("contentWidth", 500);
//                    put("contentHeight", 600);
                    put("includeViewParams", true);
                }};
            }

            @Override
            protected Map<String, List<String>> getPopupParams() {
                return null;
            }

            @Override
            protected String getOutcome() {
                return "/dialogs/country-list.xhtml";
            }
        }.show();
    }

    public void openCreateCity() {
        new PopupBuilder() {
            @Override
            protected Map<String, Object> getPopupOptions() {
                return new HashMap<String, Object>() {{
                    put("modal", true);
                    put("draggable", false);
                    put("resizable", false);
                    put("contentWidth", 380);
                    put("contentHeight", 220);
                    put("includeViewParams", true);
                }};
            }

            @Override
            protected Map<String, List<String>> getPopupParams() {
                return new HashMap<String, List<String>>() {{
                    put(CityBean.CITY_ID_KEY, Collections.singletonList(""));
                }};
            }

            @Override
            protected String getOutcome() {
                return "/dialogs/city.xhtml";
            }
        }.show();
    }

    public void openShowCities() {
        new PopupBuilder() {
            @Override
            protected Map<String, Object> getPopupOptions() {
                return new HashMap<String, Object>() {{
                    put("modal", true);
                    put("draggable", false);
                    put("resizable", false);
                    put("contentWidth", 500);
//                    put("contentHeight", 600);
                    put("includeViewParams", true);
                }};
            }

            @Override
            protected Map<String, List<String>> getPopupParams() {
                return null;
            }

            @Override
            protected String getOutcome() {
                return "/dialogs/city-list.xhtml";
            }
        }.show();
    }

    public void openCreateOffice() {
        new PopupBuilder() {
            @Override
            protected Map<String, Object> getPopupOptions() {
                return new HashMap<String, Object>() {{
                    put("modal", true);
                    put("draggable", false);
                    put("resizable", false);
                    put("contentWidth", 380);
                    put("contentHeight", 220);
                    put("includeViewParams", true);
                }};
            }

            @Override
            protected Map<String, List<String>> getPopupParams() {
                return new HashMap<String, List<String>>() {{
                    put(OfficeBean.OFFICE_ID_KEY, Collections.singletonList(""));
                }};
            }

            @Override
            protected String getOutcome() {
                return "/dialogs/office.xhtml";
            }
        }.show();
    }

public void openShowOffices() {
        new PopupBuilder() {
            @Override
            protected Map<String, Object> getPopupOptions() {
                return new HashMap<String, Object>() {{
                    put("modal", true);
                    put("draggable", false);
                    put("resizable", false);
                    put("contentWidth", 700);
//                    put("contentHeight", 600);
                    put("includeViewParams", true);
                }};
            }

            @Override
            protected Map<String, List<String>> getPopupParams() {
                return null;
            }

            @Override
            protected String getOutcome() {
                return "/dialogs/office-list.xhtml";
            }
        }.show();
    }

    public void openShowCustomers() {
        new PopupBuilder() {
            @Override
            protected Map<String, Object> getPopupOptions() {
                return new HashMap<String, Object>() {{
                    put("modal", true);
                    put("draggable", false);
                    put("resizable", false);
                    put("contentWidth", 500);
                    put("includeViewParams", true);

                }};
            }
            @Override
            protected Map<String, List<String>> getPopupParams() {
                return null;
            }
            @Override
            protected String getOutcome() {
                return "/dialogs/customer-list.xhtml";
            }

        }.show();
    }

    public void openCreateCustomer(){
        new PopupBuilder() {
            @Override
            protected Map<String, Object> getPopupOptions() {
                return new HashMap<String, Object>() {{
                    put("modal", true);
                    put("draggable", false);
                    put("resizable", false);
                    put("contentWidth", 380);
                    put("contentHeight", 220);
                    put("includeViewParams", true);
                }};
            }

            @Override
            protected Map<String, List<String>> getPopupParams() {
                return new HashMap<String, List<String>>() {{
                    put(CustomerBean.CUSTOMER_ID_KEY, Collections.singletonList(""));
                }};
            }

            @Override
            protected String getOutcome() {
                return "/dialogs/customer.xhtml";
            }
        }.show();

    }
}
