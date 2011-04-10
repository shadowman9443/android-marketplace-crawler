class AppPermission < ActiveRecord::Base
  belongs_to :app
  belongs_to :permission
end
